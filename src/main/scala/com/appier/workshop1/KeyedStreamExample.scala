package com.appier.workshop1

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.runtime.state.{FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.util.Collector

import scala.collection.JavaConverters.iterableAsScalaIterableConverter

object KeyedStreamExample {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    env
      .fromElements(
        User("user-a", 17, "user_a@yahoo.com"),
        User("user-b", 28, "user_b@gmail.com"),
        User("user-c", 33, "user_c@yahoo.com"),
        User("user-a", 18, "user_a@yahoo.com"),
        User("user-b", 28, "user_b@hotmail.com")
      )
      .keyBy(user => new UserKey(user.id))
      .flatMap {
        new RichFlatMapFunction[User, User] with CheckpointedFunction {
          // will throw NPE if you used it not after a 'keyBy()' function
          lazy val userState: ValueState[User] = getRuntimeContext.getState(
            new ValueStateDescriptor[User]("user", classOf[User]))
          lazy val users: ListState[User] = getRuntimeContext.getListState(
            new ListStateDescriptor[User]("users", classOf[User]))
          var allUsers: ListState[User] = _

          override def flatMap(value: User, out: Collector[User]): Unit = {
            println(s"old: ${userState.value()}, new: $value")
            userState.update(value)

            users.add(value)
            allUsers.add(value)
            println(s"users: ${users.get().asScala.toList.mkString(",")}")
            println(s"all users: ${allUsers.get().asScala.toList.mkString(",")}")
          }

          override def snapshotState(context: FunctionSnapshotContext): Unit = {
          }

          override def initializeState(context: FunctionInitializationContext): Unit = {
            val descriptor = new ListStateDescriptor[User]("all users", classOf[User])
            allUsers = context.getOperatorStateStore.getListState(descriptor)
          }
        }
      }

    env.execute()
  }

  case class User(id: String, age: Int, email: String)

  class UserKey(val id: String) {
    override def hashCode(): Int = id.hashCode

    override def equals(obj: Any): Boolean = obj.isInstanceOf[UserKey] && obj.asInstanceOf[UserKey].id == id
  }

}
