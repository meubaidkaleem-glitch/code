# Q4: Fork/Join framework
Classes: ParallelRiskCalculator, PortfolioThresholdTask, ForkJoinPortfolioApp
Run: javac *.java then java ForkJoinPortfolioApp

Result: 10,000 elements ran 0.10x (overhead dominates); 10,000,000 elements ran 1.48x faster in parallel.
