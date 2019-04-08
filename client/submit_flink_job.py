#!/usr/bin/env python

import json
import time
import configargparse
import requests


DEFAULT_HOST = 'http://localhost:8081'
DEFAULT_JAR_PATH = '../target/scala-2.11/appier_flink_workshop-assembly-0.0.1-SNAPSHOT.jar'
DEFAULT_ENTRY_CLASS = 'com.appier.workshop5.KafkaMessageGenerator'
DEFAULT_SAVEPOINT_DIR = 'file:///savepoints'


def upload_jar(url, jar_path):
    files = {'jarfile': open(jar_path, 'rb')}
    r = requests.post(url + '/jars/upload', files=files)
    jar_id = r.json()['filename'].split('/')[-1]
    print('upload jar: {}'.format(jar_id))
    return jar_id


def run_job(url, jar_id, entry_class, args=None, restore_path=None, parallelism=1):
    data = {
        'entryClass': entry_class,
        'allowNonRestoredState': False,
        'programArgs': args,
        'savepointPath': restore_path,
        'parallelism': parallelism
    }
    r = requests.post(url + '/jars/' + jar_id + '/run', data=json.dumps(data))
    job_id = r.json()['jobid']
    print('submit job: {}'.format(job_id))
    return job_id


def check_job_is_running(url, job_id):
    r = requests.get(url + '/jobs/' + job_id)
    return bool(r.status_code == 200 and r.json()['state'] == 'RUNNING')


def trigger_savepoint(url, job_id, cancel_job=False):
    if not check_job_is_running(url, job_id):
        raise Exception('job is not running.')

    data = {
        'cancel-job': cancel_job,
        'target-directory': DEFAULT_SAVEPOINT_DIR
    }
    r = requests.post(url + '/jobs/' + job_id + '/savepoints', data=json.dumps(data))
    request_id = r.json()['request-id']
    print('savepoint request id: {}'.format(request_id))
    return request_id


def get_savepoint_path(url, job_id, request_id):
    savepoint_path = None

    sleep = 1
    while not savepoint_path:
        r = requests.get(url + '/jobs/' + job_id + '/savepoints/' + request_id)
        if r.status_code == 200:
            if r.json()['status']['id'] == 'COMPLETED':
                savepoint_path = r.json()['operation']['location']
            else:
                print('savepoint is running. sleep {} secs.'.format(sleep))
                time.sleep(sleep)
                sleep = min(180, sleep*2)
        else:
            raise Exception(r.context)

    print('savepoint location: {}'.format(savepoint_path))
    return savepoint_path


def delete_jar(url, jar_id):
    requests.delete(url + '/jars/' + jar_id)


def list_all_jobs(url):
    r = requests.get(url + '/jobs')
    for item in r.json()['jobs']:
        print(json.dumps(item))


def submit_job(url, args):
    jar_path = DEFAULT_JAR_PATH
    entry_class = args.entry_class
    program_args = args.program_args
    parallelism = args.parallelism
    previous_job_id = args.previous_job_id

    jar_id = upload_jar(url, jar_path)

    try:
        if previous_job_id:
            print('cancel and save state from job: {}'.format(previous_job_id))
            request_id = trigger_savepoint(url, previous_job_id, cancel_job=True)
            restore_path = get_savepoint_path(url, previous_job_id, request_id)
        else:
            restore_path = args.restore_path

        if restore_path:
            print('restore job from path: {}'.format(restore_path))

        run_job(url, jar_id,
                entry_class=entry_class,
                args=program_args,
                restore_path=restore_path,
                parallelism=parallelism)
    finally:
        delete_jar(url, jar_id)
        print('delete jar')


def savepoint_without_cancel(url, args):
    job_id = args.job_id
    request_id = trigger_savepoint(url, job_id, cancel_job=False)
    get_savepoint_path(url, job_id, request_id)


def main():
    # Command-line values override environment variables which override
    # config file values which override defaults.
    parser = configargparse.ArgParser()
    subparsers = parser.add_subparsers(help='sub-command -h', dest='command')

    list_command_parser = subparsers.add_parser('list', help='list -h')
    list_command_parser.add('--host', required=False, default=DEFAULT_HOST, help='flink host')

    submit_command_parser = subparsers.add_parser('submit', help='submit -h')
    submit_command_parser.add('--host', required=False, default=DEFAULT_HOST, help='flink host')
    submit_command_parser.add('--entry-class', required=False, default=DEFAULT_ENTRY_CLASS, help='job entry class name')
    submit_command_parser.add('--program-args', required=False, default='', help='program arguments')
    submit_command_parser.add('--parallelism', required=False, default=1, help='parallelism of job')
    submit_command_parser.add('--restore-path', required=False, help='')
    submit_command_parser.add('--previous-job-id', required=False, help='')

    savepoint_command_parser = subparsers.add_parser('savepoint', help='savepoint -h')
    savepoint_command_parser.add('--host', required=False, default=DEFAULT_HOST, help='flink host')
    savepoint_command_parser.add('--job-id', required=True, help='')

    args = parser.parse_args()
    print(args)
    host = args.host

    if args.command == 'list':
        list_all_jobs(host)
    elif args.command == 'submit':
        submit_job(host, args)
    elif args.command == 'savepoint':
        savepoint_without_cancel(host, args)


if __name__ == '__main__':
    main()
