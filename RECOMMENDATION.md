# Ski Resort Recommendation System

This document explains **how the ski resort recommendation system works** and **how to call it via the API**.

---

## Overview

The recommendation system suggests the **top ski resorts** for a user based on:

* **User location (latitude & longitude)**
* **Current weather conditions**
* **Avalanche safety bulletins**
* **Realistic skiing condition thresholds**

The goal is to return **safe, nearby resorts with good and usable skiing conditions**, rather than mathematically “perfect” weather.

---

## High-Level Flow

1. Identify the **20 closest ski resorts** to the user
2. Exclude resorts located in **dangerous avalanche regions**
3. Score remaining resorts using a **penalty-based weather model**
4. Sort resorts by **lowest total penalty**
5. Return the **top 5 recommended resorts**

---

## Ideal & Acceptable Skiing Conditions

Instead of a single “perfect value”, the system uses **realistic ranges and thresholds**.

| Condition   | Acceptable / Ideal Range     |
| ----------- | ---------------------------- |
| Snow Depth  | **0.30 m – 1.50 m**          |
| Temperature | Ideal at **-4 °C**           |
| Wind Speed  | No penalty up to **15 km/h** |

Conditions outside these ranges incur penalties.

---

## Penalty Scoring Model

Each resort receives a **penalty score** based on how much its conditions deviate from safe and enjoyable skiing.

**Lower penalty = better recommendation**

---

### Snow Depth Penalty

Snow depth is treated as a **range-based factor**:

| Condition          | Penalty Logic                          |
| ------------------ | -------------------------------------- |
| `< 0.30 m (30 cm)` | Strong penalty proportional to deficit |
| `0.30 m – 1.50 m`  | **No penalty (ideal range)**           |
| `> 1.50 m`         | Small fixed penalty                    |

This reflects real skiing conditions:

* Too little snow → poor coverage & safety issues
* Very deep snow → operational difficulty (but still skiable)

---

### Temperature Penalty

Temperature is compared to the ideal skiing temperature:

```
penalty += |actualTemp − (-4°C)| × 5
```

| Importance | Reason                 |
| ---------- | ---------------------- |
| Medium     | Comfort & snow quality |

---

### Wind Speed Penalty

Wind is **only penalized when it becomes disruptive**:

| Condition   | Penalty Logic                  |
| ----------- | ------------------------------ |
| `≤ 15 km/h` | **No penalty**                 |
| `> 15 km/h` | Penalty proportional to excess |

This avoids penalizing calm or mildly windy conditions.

---

## Avalanche Safety Filtering (Hard Rule)

Before scoring, the system **fully excludes** resorts located in avalanche-danger regions.

### Dangerous avalanche levels

* Moderate
* Considerable
* High
* Very High

If a resort falls under any active bulletin with the above levels, it is **not considered at all**, regardless of weather quality.

> **Avalanche safety always overrides weather and distance.**

---

## Distance Calculation

Distances between the user and ski resorts are calculated using the **Haversine formula**, ensuring accurate Earth-surface distance measurements (in kilometers).

Distance is used to:

* Select the **20 closest resorts**
* Provide proximity context in the response

---

## Core Components

### `RecommendationService`

Responsible for:

* Distance calculations
* Avalanche safety filtering
* Weather-based penalty scoring
* Ranking and selecting top resorts

### Repositories Used

* `SkiResortRepository` – resort metadata & coordinates
* `WeatherDataRepository` – latest weather per resort
* `AvalancheDataRepository` – currently valid avalanche bulletins

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
| recommendations | Ranked list (lowest penalty first) |

---

## Design Philosophy

* **Safety-first**: avalanche risk is a hard exclusion
* **Realistic rules**: thresholds over theoretical ideals
* **Explainable logic**: deterministic, auditable scoring
* **User-centric**: prioritizes skiable, not “perfect”, conditions

---

## Future Enhancements

* User skill-level weighting (beginner vs expert)
* Crowd density & pricing data
* Open slopes & lift availability
* Machine-learning-based scoring adjustments
