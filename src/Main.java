
/*
 * David Wang
 * ECE 461
 * Mini-Project 1
 */

import java.util.*;

public class Main {

	static int packets = 1000000;
	static double lambda = 8;
	static int mu1 = 5;
	static int mu2 = 5;
	static double phi = .4;
	final static int MAXBUFFER = 5;

	public static double getPoissonRandom(double mean) { // get arrival time
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

	public static boolean chooseQueue() { // choose queue 1
		Random rand = new Random();
		double number = rand.nextDouble();

		if (number <= phi) {
			return true;
		} else {
			return false;
		}

	}

	public static double serviceTime(double mean) { // get service time
		Random rand = new Random();
		return Math.log(1 - rand.nextDouble()) / (-(mean));
	}

	/*
	 * public static double removeQueue(PriorityQueue<Double> queue, double
	 * timePassed, double arrival) { while (queue.size() > 0 && timePassed <=
	 * arrival) { timePassed += queue.peek(); queue.poll(); } if (timePassed >
	 * arrival) { queue.add(timePassed - arrival); return (timePassed - arrival); }
	 * else { return 0.0; } }
	 */

	public static void main(String[] args) {
		PriorityQueue<Double> queue1 = new PriorityQueue<Double>();
		PriorityQueue<Double> queue2 = new PriorityQueue<Double>();

		double blocked1 = 0;
		double blocked2 = 0;
		double total1 = 0;
		double total2 = 0;
		double delay1 = 0;
		double delay2 = 0;

		for (int i = 0; i < packets; ++i) { // go through each packet
			double arrival = 1 / getPoissonRandom(lambda); // arrival time of packet
			double timePassed1 = 0;
			double timePassed2 = 0;

			if (i > 100000) { // skip first few packets
				// packet arrive, add it to a queue
				if (chooseQueue()) { // if queue 1 is chosen
					total1++;
					if (queue1.size() <= MAXBUFFER) { // if buffer isn't full
						queue1.add(serviceTime(mu1)); // add service time to queue
					} else { // if buffer is full
						blocked1++; // +1 to blocked packets
					}
				} else { // if queue 2 is chose
					total2++;
					if (queue2.size() <= MAXBUFFER) {// if buffer isn't full
						queue2.add(serviceTime(mu2));// add service time to queue
					} else {// if buffer is full
						blocked2++;// +1 to blocked packets
					}
				}
			}

			// calculate when to remove from queue, if enough time pass, remove from queue
			while (queue1.size() > 0 && timePassed1 < arrival) { // while queue isn't empty and packet hasn't arrived
				timePassed1 += queue1.peek(); // service time passes
				delay1 += queue1.peek();
				queue1.poll(); // remove service time
				if (timePassed1 > arrival) { // if packet arrives
					queue1.add(timePassed1 - arrival); // add difference of time to queue
				}
			}

			// calculate when to remove from queue, if enough time pass, remove from queue
			while (queue2.size() > 0 && timePassed2 < arrival) { // while queue isn't empty and packet hasn't arrived
				timePassed2 += queue2.peek(); // service time passes
				delay2 += queue2.peek();
				queue2.poll(); // remove service time
				if (timePassed2 > arrival) { // if packet arrives
					queue2.add(timePassed2 - arrival); // add difference of time to queue
				}
			}

		}

		double blockedQ1 = blocked1 / total1;
		double blockedQ2 = blocked2 / total2;
		double blockedAll = (blocked1 + blocked2) / (total1 + total2);
		double delayQ1 = delay1 / total1;
		double delayQ2 = delay2 / total2;
		double delayAll = (delay1 + delay2) / (total1 + total2);
		double throughputQ1 = (total1 - blocked1) / total1;
		double throughputQ2 = (total2 - blocked2) / total2;
		double throughputAll = ((total1 + total2) - (blocked1 + blocked2)) / (total1 + total2);
		double averagePacketsQ1 = lambda * phi * (1 - blockedQ1) * delayQ1;
		double averagePacketsQ2 = lambda * (1 - phi) * (1 - blockedQ2) * delayQ2;
		double averagePacketsAll = lambda * (1 - blockedAll) * delayAll;

		System.out.println("Blocked 1: " + blockedQ1);
		System.out.println("Blocked 2: " + blockedQ2);
		System.out.println("Blocked System: " + blockedAll);
		System.out.println("Delay 1: " + delayQ1);
		System.out.println("delay 2: " + delayQ2);
		System.out.println("Delay System: " + delayAll);
		System.out.println("Throughput 1: " + throughputQ1);
		System.out.println("Throughput 2: " + throughputQ2);
		System.out.println("Throughput System: " + throughputAll);
		System.out.println("Average Packets 1: " + averagePacketsQ1);
		System.out.println("Average Packets 2: " + averagePacketsQ2);
		System.out.println("Average Packets System: " + averagePacketsAll);
	}

}