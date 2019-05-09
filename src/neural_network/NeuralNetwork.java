package neural_network;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class NeuralNetwork {
	
	int input_dim;
	int hidden_dim;
	int output_dim;
	
	int batch_size = 1;
	double learning_rate = 0.1;
	
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
		
		this.memory = new ReplayMemory(50000, input_dim, output_dim);
		
		// Add data
		//this.generate_memory(50000);
		
		/*for(int i = 0 ; i < 50000 ; ++i) {
			double cost = this.run_mini_batch();
			System.out.println(cost);
			System.out.println(i);
			this.generate_memory(1);
		}*/
	}
	
	public void generate_memory_unique() {
		double[][] code = new double[1][input_dim];
		for(int i = 0 ; i < output_dim ; ++i) {
			int num_out = -1;
			do {
				code = Np.random(1, input_dim);
				code = Np.multiply(5, code);
				double sum = Np.sum(code);
				num_out = (int)sum % output_dim;
			}while(num_out != i);
			double[] code_f = Np.flatten(code);
			double[] y = new double[output_dim];
			y[num_out] = 1;
			
			this.add_to_memory(code_f, y);
		}
		
		//Np.pause();
	}
	
	public void generate_memory(int n) {
		int[] num_classes = new int[5];
		
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		
		double[][] code = new double[1][input_dim];
		for(int i = 0 ; i < n; ++i) {
			code = Np.random(1, input_dim);
			code = Np.multiply(5, code);
			double sum = Np.sum(code);
			
			if(max < sum) max = sum;
			if(min > sum) min = sum;
			
			int num_out = (int)sum % output_dim;
			
			double[] code_f = Np.flatten(code);
			double[] y = new double[output_dim];
			y[num_out] = 1;
			
			this.add_to_memory(code_f, y);
			
			num_classes[num_out]++;
		}
		
		/*System.out.println(max);
		System.out.println(min);*/
		
		/*for(int num_c : num_classes) {
			System.out.println(num_c);
		}*/
		
		/*try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		int argmax = Np.argmax(output_f); 
		
		return argmax;
	}
	
	public double train(double[][] input, double[][] label) {		
		return train_cross_entropy(input, label);
		//return train_mse(input, label);
	}
	
	
	public double train_cross_entropy(double[][] input, double[][] label) {
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
		//double cost = Np.mean_squared_error(batch_size, label, a2);
		
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
	
	
	public double train_mse(double[][] input, double[][] label) {
		double[][] z1, a1, z2, a2;
		
		// === Forward pass ===
		z1 = Np.add(Np.dot(w1, input), b1);		
		//a1 = Np.relu(z1);
		a1 = Np.sigmoid(z1);
		
		z2 = Np.add(Np.dot(w2, a1), b2);
		//a2 = Np.softmax(z2);
		a2 = Np.sigmoid(z2);
		// ====================
		
		//double cost = Np.cross_entropy(batch_size, label, a2);
		double cost = Np.mean_squared_error(batch_size, label, a2);
		
		// ===== Backprop =====
		double[][] sig_der = Np.sigmoid_derivative(a2);
		
		double[][] dZ2 = Np.multiply(Np.subtract(a2, label), sig_der);
		double[][] dW2 = Np.dot(dZ2, Np.T(a1));
		double[][] db2 = dZ2;
		
		sig_der = Np.sigmoid_derivative(a1);
		
		double[][] dZ1 = Np.multiply(Np.dot(Np.T(w2), dZ2), sig_der);
		double[][] dW1 = Np.dot(dZ1, input);
		double[][] db1 = dZ1;

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
		}
		
		return cost;
	}
	
	public double test(double[][][] input, int[] label) {
		double acc = 0;
		
		for(int i = 0 ; i < input.length ; ++i) {
			if(label[i] == forward_argmax(input[i]))
				acc++;
		}
		
		return acc / label.length;
	}
	
	
	
	
	
	public static void main(String[] args) {		
		/*NeuralNetwork nn = new NeuralNetwork(2, 8, 1);
		
		double[][] X = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        double[][] Y = {{0}, {1}, {1}, {0}};
        
        System.out.println(Np.shape(X));
		System.out.println(Np.shape(Y));
        
        for(int i = 0 ; i < X.length ; ++i) {
        	nn.add_to_memory(X[i], Y[i]);
        }
		
		for(int i = 0 ; i < 50000 ; ++i) {
			//double cost = nn.train(X, Y);
			double cost = nn.run_mini_batch();
			System.out.println(cost);
		}
		
		for(int i = 0 ; i < 50 ; ++i) {
			double[][] in = nn.memory.get_batch_x(1);
			double[][] out = nn.memory.get_batch_y();
			System.out.println(nn.forward_argmax(in));	
			System.out.println(out[0][0]);
		}*/
		
		NeuralNetwork nn = new NeuralNetwork(1, 64, 5);
		
		for(int i = 0 ; i < 500000 ; ++i) {
			//double cost = nn.train(X, Y);
			double cost = nn.run_mini_batch();
			System.out.println(i);
			System.out.println(cost);
			nn.generate_memory(1);
		}
		
		int test_size = 50000;
		int code_size = (int)Math.sqrt(nn.input_dim);
		double[][][] test_x = new double[test_size][1][nn.input_dim];
		int[] test_y = new int[test_size];
		for(int i = 0 ; i < test_size; ++i) {
			double[][] code = Np.random(code_size, code_size);
			code = Np.multiply(5, code);
			test_x[i] = Np.code_to_input(code, code_size);
			//test_x[i] = Np.T(code);
			int num_button = (int)Np.sum(code) % nn.output_dim;
			test_y[i] = num_button;
		}
		
		double test_acc = nn.test(test_x, test_y);
		System.out.println(test_acc);
		
		
	}
	
}