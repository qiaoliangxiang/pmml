import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class PmmlUtil {

    /**
     * Loads PMML file.
     *
     * @param fp file path
     * @return an instance of `Evaluator`
     * @throws FileNotFoundException if the file cannot be found
     * @throws JAXBException
     * @throws SAXException
     */
    public Evaluator loadPmml(String fp) throws FileNotFoundException, JAXBException, SAXException {
        InputStream is = new FileInputStream(fp);
        PMML pmml = org.jpmml.model.PMMLUtil.unmarshal(is);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ModelEvaluatorFactory factory = ModelEvaluatorFactory.newInstance();
        return factory.newModelEvaluator(pmml);
    }

    /**
     * Predict a model.
     *
     * @param evaluator an instance of evaluator
     * @param input a map of input field names and values
     * @return a map of output field names and values
     */
    public Map<String, Object> predict(Evaluator evaluator, Map<String, Object> input) {
        Map<FieldName, FieldValue> pmmlInput = getFieldMap(evaluator, input);
        Map<String, Object> output = evaluate(evaluator, pmmlInput);
        return output;
    }

    /**
     * Converts a normal map to a field map.
     *
     * @param evaluator evaluator an instance of Evaluator
     * @param input input map
     * @return output field map
     */
    public Map<FieldName, FieldValue> getFieldMap(Evaluator evaluator, Map<String, Object> input) {
        List<InputField> inputFields = evaluator.getInputFields();
        Map<FieldName, FieldValue> map = new LinkedHashMap<FieldName, FieldValue>();
        for (InputField field : inputFields) {
            FieldName fieldName = field.getName();
            Object rawValue = input.get(fieldName.getValue());
            FieldValue value = field.prepare(rawValue);
            map.put(fieldName, value);
        }
        return map;
    }

    /**
     * Evaluates a model.
     *
     * @param evaluator an instance of Evaluator
     * @param input a map of input field names and values
     * @return a map of output field names and values
     */
    public Map<String, Object> evaluate(Evaluator evaluator, Map<FieldName, FieldValue> input) {
        Map<FieldName, ?> results = evaluator.evaluate(input);
        List<TargetField> targetFields = evaluator.getTargetFields();
        Map<String, Object> output = new LinkedHashMap<String, Object>();
        for (int i = 0; i < targetFields.size(); i++) {
            TargetField field = targetFields.get(i);
            FieldName fieldName = field.getName();
            Object value = results.get(fieldName);
            if (value instanceof Computable) {
                Computable computable = (Computable) value;
                value = computable.getResult();
            }
            output.put(fieldName.getValue(), value);
        }
        return output;
    }

}
