package com.appier.workshop1

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.util.Collector

object KeyedStreamExample {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

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
        new RichFlatMapFunction[User, User] {
          // will throw NPE if you used it not after a 'keyBy()' function
          lazy val userState: ValueState[User] = getRuntimeContext.getState(
            new ValueStateDescriptor[User]("user", classOf[User]))

          override def flatMap(value: User, out: Collector[User]): Unit = {
            println(s"old: ${userState.value()}, new: $value")
            userState.update(value)
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
