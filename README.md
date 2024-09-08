# BellmanFord Arbitrage Cycle Finder

This project implements the Bellman-Ford algorithm to detect arbitrage cycles in a given set of currency exchange rates.
The application fetches exchange rates from a specified URL and processes them to find potential arbitrage opportunities.
It then prints the arbitrage cycles found, if any.

## Getting Started

### Running the Application

To compile and run the application, navigate to the project directory and use the following commands:

#### Compile
```sh
scala-cli compile .
```

#### Run Tests
```sh
scala-cli test .
```

#### Run Application
```sh
scala-cli run . -- https://api.swissborg.io/v1/challenge/rates
```

### Arguments

- **URL**: The URL from which to fetch the exchange rates. If no URL is provided, the default URL `https://api.swissborg.io/v1/challenge/rates` will be used.

### Example

```sh
scala-cli run . -- https://api.swissborg.io/v1/challenge/rates
```

## Bellman-Ford Algorithm Complexity

The Bellman-Ford algorithm has a time complexity of **O(V*E)**, where **V** is the number of vertices (nodes) and **E** is the number of edges in the graph.
In our case each currency is a node(V), and each rate an edge(E).

A Breakdown of the Time Complexity:
1. Initialization: Initializing the distance and predecessor maps takes O(V) time.
2. Relaxation: The relaxation step runs for V - 1 iterations, and in each iteration, it processes all E edges. This results in O(V * E) time.
3. Negative-Weight Cycle Check: This step also processes all E edges, taking O(E) time.

Since the relaxation step dominates the overall time complexity, the total time complexity is O(V * E).

## About Borg Token
The Borg token (BORG) is a utility token and a key component of the SwissBorg ecosystem.

- Governance: Borg holders can participate in platform governance and vote on proposals, influencing the direction of the SwissBorg ecosystem.
- Premium Benefits: By locking Borg tokens, users can access SwissBorg Premium, which offers reduced fees and yield boosts based on the amount locked.
- Borger Journey: Borg holders can engage in the Borger Journey, a gamified experience designed to educate users about the SwissBorg platform while unlocking exclusive opportunities and rewards.
- BORG Protect & Choose: A percentage of revenue earned from exchange fees is used to buy back BORG tokens. Additionally, Borg holders can decide quarterly how these tokens will be utilized, providing a say in the token's future.
- Buyback Program: SwissBorg may buy back Borg tokens based on the Community Level Index, which measures community engagement and activity.
