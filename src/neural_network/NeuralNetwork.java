package neural_network;

public class NeuralNetwork {
	
	int input_dim;
	int hidden_dim;
	int output_dim;
	
	int batch_size = 1;
	double learning_rate = 0.01;
	
	ReplayMemory memory;
	
	double[][] w1;
	double[][] b1;
	
	double[][] w2;
	double[][] b2;

	public NeuralNetwork(int input_dim, int hidden_dim, int output_dim) {		
		/*w1 = Np.random(input_dim, hidden_dim);
		b1 = new double[batch_size][hidden_dim];
		
		w2 = Np.random(hidden_dim, output_dim);
		b2 = new double[batch_size][output_dim];*/
		
		w1 = Np.random(hidden_dim, input_dim);
		b1 = new double[hidden_dim][batch_size];
		
		w2 = Np.random(output_dim, hidden_dim);
		b2 = new double[output_dim][batch_size];
		
		this.input_dim = input_dim;
		this.hidden_dim = hidden_dim;
		this.output_dim = output_dim;
		
		this.memory = new ReplayMemory(5000, input_dim, output_dim);
		
		// Add data
		/*int code_size = (int)Math.sqrt(input_dim);
		double[][] code = new double[code_size][code_size];
		for(int i = 0 ; i < 5000; ++i) {
			code = Np.random(code_size, code_size);
			int num_button = (int) (Math.random() * (output_dim - 1));
			double[] code_f = Np.flatten(code);
			double[] y = new double[output_dim];
			y[num_button] = 1;
			
			this.add_to_memory(code_f, y);
		}*/
	}
	
	public void randomize_bias() {
		b1 = Np.random(hidden_dim, batch_size);
		b2 = Np.random(hidden_dim, batch_size);
	}
	
	public void add_to_memory(double[] x, double[] y) {
		this.memory.add(x, y);
	}
	
	public double[][] forward(double[][] input) {
		double[][] z1, a1, z2, a2;
		
		z1 = Np.add(Np.dot(w1, input), b1);
		//a1 = Np.relu(z1);
		a1 = Np.sigmoid(z1);
		
		z2 = Np.add(Np.dot(w2, a1), b2);
		//a2 = Np.softmax(z2);
		a2 = Np.sigmoid(z2);
		
		return a2;
	}
	
	public int forward_argmax(double [][] input) {
		double[][] ouput = this.forward(input);
		double[] output_f = Np.flatten(ouput);
		return Np.argmax(output_f);
	}
	
	public double train(double[][] input, double[][] label) {
		double[][] z1, a1, z2, a2;
		
		// === Forward pass ===				
		z1 = Np.add(Np.dot(w1, input), b1);		
		//a1 = Np.relu(z1);
		a1 = Np.sigmoid(z1);
		
		z2 = Np.add(Np.dot(w2, a1), b2);
		//a2 = Np.softmax(z2);
		a2 = Np.sigmoid(z2);
		// ====================
		
		double cost = Np.cross_entropy(batch_size, label, a2);
		
		// ===== Backprop =====
		double[][] dZ2 = Np.subtract(a2, label);
        double[][] dW2 = Np.divide(Np.dot(dZ2, Np.T(a1)), batch_size);
        double[][] db2 = Np.divide(dZ2, batch_size);

        double[][] dZ1 = Np.multiply(Np.dot(Np.T(w2), dZ2), Np.subtract(1.0, Np.power(a1, 2)));
        double[][] dW1 = Np.divide(Np.dot(dZ1, Np.T(input)), batch_size);
        double[][] db1 = Np.divide(dZ1, batch_size);


        w1 = Np.subtract(w1, Np.multiply(learning_rate, dW1));
        b1 = Np.subtract(b1, Np.multiply(learning_rate, db1));

        w2 = Np.subtract(w2, Np.multiply(learning_rate, dW2));
        b2 = Np.subtract(b2, Np.multiply(learning_rate, db2));
		// ====================
		
		return cost;
	}
	
	public double run_mini_batch() {
		double cost = -1;
		if(this.memory.getMemory_size() >= batch_size) {
			double[][] batchX = memory.get_batch_x(batch_size);
			double[][] batchY = memory.get_batch_y();
			
			cost = this.train(batchX, batchY);	
			//System.out.println(cost);
		}
		
		return cost;
	}
	
	public static void main(String[] args) {
		/*NeuralNetwork nn = new NeuralNetwork(16, 8, 5);
		
		double[][] input = Np.random(1, 16);
		
		double[][] out = nn.forward(input);*/
		
		NeuralNetwork nn = new NeuralNetwork(2, 8, 2);
		
		double[][] X = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        double[][] Y = {{0,1}, {1,1}, {1,1}, {0,1}};
        
        System.out.println(Np.shape(X));
		System.out.println(Np.shape(Y));
        
        for(int i = 0 ; i < X.length ; ++i) {
        	nn.add_to_memory(X[i], Y[i]);
        }
        
        X = Np.T(X);
		Y = Np.T(Y);
		
		System.out.println(Np.shape(X));
		System.out.println(Np.shape(Y));
		
		for(int i = 0 ; i < 40000 ; ++i) {
			//double cost = nn.train(X, Y);
			double cost = nn.run_mini_batch();
			System.out.println(cost);
		}
		
		
	}
	
}




/*import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;*/

/*MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
.seed(seed)
.weightInit(WeightInit.XAVIER)
.updater(new Nesterovs(learningRate, 0.9))
.list()
.layer(new DenseLayer.Builder()
    .nIn(numInputs)
    .nOut(numHiddenNodes)
    .activation(Activation.RELU)
    .build())
.layer(new OutputLayer.Builder(LossFunction.XENT)
    .nIn(numHiddenNodes)
    .nOut(numOutputs)
    .activation(Activation.SIGMOID)
    .build())
.build();*/