# How To Use

## Requirements
```bash
pip install -r requirements.txt
```

## List Jobs
```bash
$ python submit_flink_job.py list -h
usage: submit_flink_job.py list [-h] [--host HOST]

optional arguments:
  -h, --help   show this help message and exit
  --host HOST  flink host
```

## Submit Job
```bash
$ python submit_flink_job.py submit -h
usage: submit_flink_job.py submit [-h] [--host HOST]
                                  [--entry-class ENTRY_CLASS]
                                  [--program-args PROGRAM_ARGS]
                                  [--parallelism PARALLELISM]
                                  [--restore-path RESTORE_PATH]
                                  [--previous-job-id PREVIOUS_JOB_ID]

optional arguments:
  -h, --help            show this help message and exit
  --host HOST           flink host
  --entry-class ENTRY_CLASS
                        job entry class name
  --program-args PROGRAM_ARGS
                        program arguments
  --parallelism PARALLELISM
                        parallelism of job
  --restore-path RESTORE_PATH
  --previous-job-id PREVIOUS_JOB_ID
```

## Trigger Savepoint
```bash
i$ python submit_flink_job.py savepoint -h
usage: submit_flink_job.py savepoint [-h] [--host HOST] --job-id JOB_ID

optional arguments:
  -h, --help       show this help message and exit
  --host HOST      flink host
  --job-id JOB_ID
```