package neural_network;

import java.util.HashMap;
import java.util.Map;

public class ReplayMemory {
	private double[][] X;
	private double[][] Y;
	private int index = 0;
	private int memory_size = 0;
	private int memory_capacity;
	
	private int input_size;
	private int output_size;
	
	private int[] batch_ind = {0,1,2,3};
	
	public ReplayMemory(int memory_capacity, int input_size, int output_size) {
		X = new double[memory_capacity][input_size];
		Y = new double[memory_capacity][output_size];
		
		this.input_size = input_size;
		this.output_size = output_size;
		this.memory_capacity = memory_capacity;
	}
	
	public void add(double[] x, double[] y) {
		X[index] = x;
		Y[index] = y;
		index = (index + 1) % memory_capacity;
		++memory_size;
	}
	
	public int[] random(int n) {
		int[] rand = new int[n];
		
		Map<Integer, Integer> rand_map = new HashMap();
		
		int nb = 0;
		
		while(nb < n) {
			int ind = (int)(Math.random() * (memory_size));
			if( ! rand_map.containsKey(ind)) {
				rand[nb] = ind;
				rand_map.put(ind, ind);
				++nb;
			}
		}
		
		return rand;
	}
	
	public double[][] get_batch_x(int batch_size){
		double[][] batch = new double[batch_size][input_size];
		batch_ind = random(batch_size);
		
		for(int i = 0 ; i < batch_size; ++i)
			batch[i] = X[batch_ind[i]];
		
		return Np.T(batch);
	}
	
	public double[][] get_batch_y(){
		int batch_size = batch_ind.length;
		double[][] batch = new double[batch_size][output_size];
		
		for(int i = 0 ; i < batch_size; ++i)
			batch[i] = Y[batch_ind[i]];
			
		
		return Np.T(batch);
	}

	public int getMemory_size() {
		return memory_size;
	}	
	
}
