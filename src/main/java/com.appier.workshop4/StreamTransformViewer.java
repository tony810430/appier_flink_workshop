package com.appier.workshop4;

import org.apache.flink.api.dag.Transformation;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.transformations.OneInputTransformation;
import org.apache.flink.streaming.api.transformations.PartitionTransformation;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.lang.reflect.Field;
import java.util.List;


public class StreamTransformViewer {
    public static void main(String[] args) throws Exception {
        // set up streaming execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        User[] users = {
          new User("UID_0001", "Amy", 27),
          new User("UID_0002", "Tom", 17)
        };

        KeyedStream<User, String> keyedUser = env
          // Flink didn't allow a job with only source functions.
          .fromElements(users)
          // "Assign timestamp" is an operator.
          .assignTimestampsAndWatermarks(new AscendingTimestampExtractor<User>() {
              @Override
              public long extractAscendingTimestamp(User element) {
                  return element.timestamp;
              }
          })
          .filter(value -> value.age > 5L)
          // "keyBy" will do nothing if there is no function behind it.
          .keyBy(value -> value.id);

        keyedUser.timeWindow(Time.seconds(1L))
          .apply(new DummyWindowFunction());

        listStreamTransformations(env);
        // env.execute("stream transform viewer");
    }

    private static void listStreamTransformations(StreamExecutionEnvironment env) throws Exception {
        Field transformationsField = env.getClass().getSuperclass().getDeclaredField("transformations");
        transformationsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Transformation> transformations = (List<Transformation>) transformationsField.get(env);

        for (Transformation transformation : transformations) {
            System.out.println(transformation.getClass());
            recursivePrintInputTransform(transformation, 1);
        }
    }

    private static void recursivePrintInputTransform(Transformation transformation, int depth) throws Exception {
        if (transformation instanceof OneInputTransformation) {
            Field keySelectorFiled = ((OneInputTransformation) transformation).getClass().getDeclaredField("stateKeySelector");
            keySelectorFiled.setAccessible(true);
            if (keySelectorFiled.get(transformation) != null)
                print("[KEY]: " + keySelectorFiled.get(transformation).getClass().toString(), depth);

            OneInputTransformation in = ((OneInputTransformation) transformation);
            Field inputField = in.getClass().getDeclaredField("input");
            inputField.setAccessible(true);
            print(inputField.get(in).getClass(), depth);
            recursivePrintInputTransform((Transformation) inputField.get(in), depth + 1);
        } else if (transformation instanceof PartitionTransformation) {
            PartitionTransformation in = ((PartitionTransformation) transformation);
            Field inputField = in.getClass().getDeclaredField("input");
            inputField.setAccessible(true);
            print(inputField.get(in).getClass(), depth);
            recursivePrintInputTransform((Transformation) inputField.get(in), depth + 1);
        }
    }

    private static void print(Object clazz, int depth) {
        for (int i = 0; i < depth; i++)
            System.out.print("    ");
        System.out.println("> " + clazz);
    }

    private static class DummyWindowFunction implements WindowFunction<User, Object, String, TimeWindow> {
        @Override
        public void apply(String s, TimeWindow window, Iterable<User> input, Collector<Object> out) {
        }
    }
}
