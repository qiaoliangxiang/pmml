from sklearn import tree
from sklearn.datasets import load_iris
from sklearn2pmml.pipeline import PMMLPipeline
from sklearn2pmml import sklearn2pmml

if __name__ == '__main__':
    fp = "iris.pmml"
    iris = load_iris()  # load the famous dat set
    X = iris.data  # features with four variables
    y = iris.target  # training target
    pipeline = PMMLPipeline([
        ("classifier", tree.DecisionTreeClassifier())
    ])
    pipeline.fit(X, y)  # fit the model
    sklearn2pmml(pipeline, fp, with_repr=True)  # create output file

    # features and targets
    print(f'features: {X[0, :]}, target: {y[0]}')
    print(f'features: {X[1, :]}, target: {y[1]}')
