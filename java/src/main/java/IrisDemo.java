import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IrisDemo {
    public static void main(String args[]) throws Exception {
        String fp = "iris.pmml";
        IrisDemo iris = new IrisDemo();
        PmmlPredictor predictor = new PmmlPredictor(fp);
        List<Map<String, Object>> inputs = new ArrayList<>();
        inputs.add(iris.getRawMap(5.1, 3.5, 1.4, 0.2));
        inputs.add(iris.getRawMap(4.9, 3, 1.4, 0.2));
        for (int i = 0; i < inputs.size(); i++) {
            Map<String, Object> output = predictor.predict(inputs.get(i));
            System.out.println("X=" + inputs.get(i) + " -> y=" + output.get("y"));
        }
    }

    private Map<String, Object> getRawMap(Object a, Object b, Object c, Object d) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("x1", a);
        data.put("x2", b);
        data.put("x3", c);
        data.put("x4", d);
        return data;
    }

}
