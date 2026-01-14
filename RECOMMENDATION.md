# Ski Resort Recommendation System

This document explains **how the ski resort recommendation system works** and **how to call it via the API**.

## Overview

The recommendation system suggests the **top ski resorts** for a user based on:

* **User location (latitude & longitude)**
* **Current weather conditions**
* **Avalanche safety bulletins**
* **Ideal skiing conditions**

The goal is to return **safe, nearby resorts with the best current skiing conditions**.

## High-Level Flow

1. Identify the **20 closest ski resorts** to the user
2. Exclude resorts located in **dangerous avalanche regions**
3. Score remaining resorts using a **penalty-based weather model**
4. Sort by lowest penalty score
5. Return the **top 5 recommended resorts**

## Ideal Skiing Conditions

The system defines a baseline for a "perfect" ski day:

| Condition   | Ideal Value |
| ----------- | ----------- |
| Snow Depth  | 0.60 m      |
| Temperature | -4 °C       |
| Wind Speed  | 10 km/h     |

Real-world conditions are compared against these values to calculate penalties.

## Penalty Scoring Model

Each resort receives a **penalty score** based on how far its weather deviates from ideal conditions.

| Factor      | Formula | Weight         | Importance |        |                  |
| ----------- | ------- | -------------- | ---------- | ------ | ---------------- |
| Snow depth  | `       | actual - ideal | × 100`     | High   | Ski quality      |
| Temperature | `       | actual - ideal | × 5`       | Medium | Comfort          |
| Wind speed  | `       | actual - ideal | × 2`       | Low    | Safety & comfort |

**Lower penalty = better recommendation**

## Avalanche Safety Filtering

Before scoring, the system removes any resorts located in avalanche-danger regions.

### Dangerous avalanche levels

* Moderate
* Considerable
* High
* Very High

If a resort is located in a region with any of the above active bulletins, it is **fully excluded** from recommendations.

## Distance Calculation

Distances between the user and ski resorts are calculated using the **Haversine formula**, ensuring accurate Earth-surface distance measurements in kilometers.

Distance is used to:

* Identify the 20 closest resorts
* Display proximity information to the user

---

## Core Components

### `RecommendationService`

* Performs distance calculation
* Filters avalanche-risk resorts
* Computes penalty scores
* Produces ranked recommendations

### Repositories Used

* `SkiResortRepository` – resort metadata & locations
* `WeatherDataRepository` – latest weather per resort
* `AvalancheDataRepository` – active avalanche bulletins

---

## API Usage

### Endpoint

```
POST /api/recommendation/skiresort
```

---

### Request Body

```json
{
  "latitude": 47.3686,
  "longitude": 8.5392
}
```

| Field     | Type   | Description    |
| --------- | ------ | -------------- |
| latitude  | number | User latitude  |
| longitude | number | User longitude |

---

### Successful Response

```json
{
  "status": "success",
  "count": 5,
  "recommendations": [
    {
      "resort": { /* ski resort data */ },
      "distance": 32.5,
      "penaltyScore": 18.7
    }
  ]
}
```

| Field           | Description                        |
| --------------- | ---------------------------------- |
| status          | Request status                     |
| count           | Number of resorts returned         |
| recommendations | Ranked list of recommended resorts |

---

## Design Philosophy

* **Safety-first**: avalanche risks override all other factors
* **Explainable logic**: deterministic scoring, easy to audit
* **Extensible**: can be enhanced with user preferences or ML models

---

## Future Enhancements

* User skill level weighting
* Crowd density & pricing
* Resort capacity & open slopes
* Machine-learning-based scoring
