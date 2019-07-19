import org.jpmml.evaluator.Evaluator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Map;


public class PmmlPredictor {
    private String fp;
    private Evaluator evaluator;
    private PmmlUtil pmmlUtil = new PmmlUtil();

    /**
     * Constructor.
     *
     * @param fp PMML file path
     */
    public PmmlPredictor(String fp) throws FileNotFoundException, JAXBException, SAXException {
        this.fp = fp;
        this.evaluator = pmmlUtil.loadPmml(fp);
    }

    /**
     * Predict.
     *
     * @param input a map of input field names and vlaues
     * @return a map of output field names and vlaues
     */
    public Map<String, Object> predict(Map<String, Object> input) {
        return pmmlUtil.predict(evaluator, input);
    }

}
