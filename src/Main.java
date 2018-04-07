
/*
 * David Wang
 * ECE 461
 * Mini-Project 1
 */

import java.util.*;

public class Main {

	static int packets = 100000;
	static int lambda = 8;
	static int mu1 = 5;
	static int mu2 = 5;
	final static int MAXBUFFER = 5;

	public static int getPoissonRandom(double mean) {
		Random r = new Random();
		double L = Math.exp(-mean);
		int k = 0;
		double p = 1.0;
		do {
			p = p * r.nextDouble();
			k++;
		} while (p > L);
		return k - 1;
	}

	public static boolean chooseQueue() {
		Random rand = new Random();
		double phi = 0.5;
		double number = rand.nextDouble();
		if (number <= phi) {
			return true;
		} else {
			return false;
		}
	}

	public static double serviceTime(double mean) {
		Random rand = new Random();
		return Math.log(1 - rand.nextDouble()) / (-(1 / mean));
	}

	public static void main(String[] args) {
		PriorityQueue<Double> queue1 = new PriorityQueue<Double>();
		PriorityQueue<Double> queue2 = new PriorityQueue<Double>();
		double blocked1 = 0.0;
		double blocked2 = 0.0;
		double total1 = 0.0;
		double total2 = 0.0;
		double delay1 = 0;
		double delay2 = 0;

		for (int i = 0; i < packets; ++i) { // go through each packet
			double arrival = getPoissonRandom(lambda); // arrival time of packet
			double service1 = serviceTime(mu1); // service times of each queue
			double service2 = serviceTime(mu2);
			double arrival1 = arrival;
			double arrival2 = arrival;

			if (i > 1000) {

				if (chooseQueue()) { // if queue 1 is chosen
					total1++;
					if (queue1.size() < MAXBUFFER) { // if buffer is not full
						queue1.add(service1); // add packet servicing time to queue

						while (arrival1 > 0 && queue1.size() > 0) { // while packet still arriving
							arrival1 = queue1.peek()-arrival1; // subtract servicing time from arrival
							if (arrival1 > 0) { // if packet still arriving
								queue1.poll(); // remove packet in queue
							} else { // if packet arrives
								queue1.add(Math.abs(arrival1)); // remove packet and add time difference
								delay1 += Math.abs(arrival1);
								queue1.poll();
							}
						}

					} else {
						blocked1++;
					}

					while (arrival2 > 0 && queue2.size() > 0) { // while packet still arriving
						arrival2 = queue2.peek()-arrival2;
						if (arrival2 > 0) { // if packet still arriving
							queue2.poll(); // remove packet in queue
						} else { // if packet arrives
							queue2.add(Math.abs(arrival2)); // remove packet and add time difference
							delay2 += Math.abs(arrival2);
							queue2.poll();
						}
					}
				}
//////////////////////////////////////////////////////QUEUE2///////////////////////////////////////////////////////////
				else { // if queue 2 is chosen
					total2++;
					if (queue2.size() < MAXBUFFER) { // if buffer is not full
						queue2.add(service2); // add packet servicing time to queue

						while (arrival2 > 0 && queue2.size() > 0) { // while packet still arriving
							arrival2 = queue2.peek()-arrival2; // subtract servicing time from arrival
							if (arrival2 > 0) { // if packet still arriving
								queue2.poll(); // remove packet in queue
							} else { // if packet arrives
								queue2.add(Math.abs(arrival2)); // remove packet and add time difference
								delay2 += Math.abs(arrival2);
								queue2.poll();
							}
						}

					} else {
						blocked2++;
					}

					while (arrival1 > 0 && queue1.size() > 0) { // while packet still arriving
						arrival1 = queue1.peek()-arrival1;
						if (arrival1 > 0) { // if packet still arriving
							queue1.poll(); // remove packet in queue
						} else { // if packet arrives
							queue1.add(Math.abs(arrival1)); // remove packet and add time difference
							delay1 += Math.abs(arrival1);
							queue1.poll();
						}
					}
				}
			}
		}

		System.out.println("Blocked 1: " + blocked1 / total1);
		System.out.println("Blocked 2: " + blocked2 / total2);
		System.out.println("Blocked System: " + (blocked1 + blocked2) / (total1 + total2));
		System.out.println("Delay 1: " + delay1);
		System.out.println("Delay 2: " + delay2);
		System.out.println("Blocked 2: " + blocked2);
		System.out.println("Blocked 1: " + blocked1);
		System.out.println("Blocked 2: " + blocked2);
	}
}