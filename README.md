# StockFlow Intelligence

StockFlow Intelligence is a demand forecasting service designed to extend the StockFlow ecosystem with statistical analysis and machine learning.

The project uses historical stock-out movements to build daily demand time series, generate predictive features, compare a machine learning model against a statistical baseline, and expose forecasts through a REST API.

## Goals

The first version focuses on a small and explainable forecasting pipeline:

Historical stock movements -> Daily aggregation -> Feature engineering -> Baseline and ML model -> Evaluation -> Forecast API

The statistical baseline will use a 7-day moving average.

The machine learning model will use features such as day of week, lagged demand, moving average, and rolling standard deviation.

Model evaluation will use a temporal train/test split and compare results using MAE and RMSE.

## Planned Stack

Java 25

Spring Boot 3.5

SMILE

Apache Commons Statistics

JUnit 5

AssertJ

GitHub Actions

## Scope

The initial version forecasts demand for one product at a time.

The objective is not to maximize prediction accuracy, but to demonstrate a correct and reproducible machine learning workflow integrated into a Java backend application.

Advanced topics such as deep learning, LLMs, RAG, distributed systems, MLOps platforms, dashboards, and Kubernetes are intentionally outside the scope of the first version.
