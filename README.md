# 📈 Kotlin Multiplatform Crypto Trading — Compose App

This repository contains a Compose Multiplatform application showcasing a simple crypto trading flow (Buy/Sell) with shared UI and state across Android and iOS. It demonstrates modern KMP development practices with a clean architecture approach.

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              PLATFORM HOSTS                                  │
├─────────────────────────────────┬───────────────────────────────────────────┤
│         Android App             │              iOS App                       │
│     (Compose Activity)          │     (SwiftUI ContentView.swift)            │
└────────────────┬────────────────┴───────────────────┬───────────────────────┘
                 │                                    │
                 │            renders                 │ embeds
                 ▼                                    ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    SHARED KMP MODULE: composeApp                             │
├─────────────────────────────────────────────────────────────────────────────┤
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │                        PRESENTATION LAYER                              │  │
│  │  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────┐    │  │
│  │  │   TradeScreen   │◄─│   TradeState    │  │     TradeType       │    │  │
│  │  │  (Composable)   │  │   (UI State)    │  │   (BUY / SELL)      │    │  │
│  │  └────────┬────────┘  └────────┬────────┘  └─────────────────────┘    │  │
│  └───────────┼────────────────────┼──────────────────────────────────────┘  │
│              │                    │                                          │
│  ┌───────────┼────────────────────┼──────────────────────────────────────┐  │
│  │           │      DOMAIN LAYER  │                                       │  │
│  │           │    ┌───────────────▼───────────────┐                       │  │
│  │           │    │       UiTradeCoinItem         │                       │  │
│  │           │    │  (id, name, symbol, price)    │                       │  │
│  │           │    └───────────────────────────────┘                       │  │
│  └───────────┼────────────────────────────────────────────────────────────┘  │
│              │                                                               │
│  ┌───────────┼────────────────────────────────────────────────────────────┐  │
│  │           │              CORE LAYER                                     │  │
│  │           ▼                                                             │  │
│  │  ┌─────────────────────┐        ┌─────────────────────────────────┐    │  │
│  │  │  PortfolioDatabase  │        │     Generated Resources         │    │  │
│  │  │       (Room)        │        │       (Res.string.*)            │    │  │
│  │  └─────────────────────┘        └─────────────────────────────────┘    │  │
│  └────────────────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────────┘
                                       ▲
                                       │ tests
┌──────────────────────────────────────┴──────────────────────────────────────┐
│                         TEST LAYER: commonTest                               │
│                  ┌─────────────────────────────────────┐                     │
│                  │   Compose UI Tests (BuyScreenTest)  │                     │
│                  └─────────────────────────────────────┘                     │
└─────────────────────────────────────────────────────────────────────────────┘
```


## 🚀 Features

- **Shared Compose UI** for Android and iOS platforms
- **Trade Screen** supporting BUY and SELL flows
- Dynamic submit CTA based on trade type (`Buy Now` / `Sell Now`)
- Coin details display (name, symbol, price, icon)
- Error state handling with shared string resources
- Local persistence via Room database (`PortfolioDatabase`)
- Comprehensive Compose UI tests validating core interactions

## ⚙️ Tech Stack

| Category | Technology |
|----------|------------|
| Language | Kotlin (+ Swift for iOS host) |
| UI | Compose Multiplatform |
| Architecture | MVVM + Clean Architecture |
| Build System | Gradle Kotlin DSL |
| Persistence | Room Database |
| Code Generation | KSP |
| Testing | Compose UI Testing (commonTest) |
| Platforms | Android + iOS |

## 🧠 Key Highlights

- Single shared UI layer for Android and iOS via Compose Multiplatform
- Clear state-driven UI (`TradeState`, `TradeType`, `UiTradeCoinItem`)
- Proper separation of concerns across presentation, domain, and data layers
- Automated UI verification using Compose tests in `composeApp/src/commonTest`
- Generated resources consumed in shared code
- Scalable and maintainable codebase
- Proper handling of light / dark mode

## 🏞️ Screenshots from both platforms

<img width="270" height="585" alt="android_1" src="https://github.com/user-attachments/assets/c9d15de6-391a-4859-95c0-d5f7772a8ad5" /> <img width="270" height="585" alt="android_2" src="https://github.com/user-attachments/assets/fefd4710-406b-433d-a1dd-20b0213be62c" /> <img width="270" height="585" alt="android_3" src="https://github.com/user-attachments/assets/229a4c90-1911-435f-aabd-f667d4171a30" /> <img width="270" height="585" alt="ios_1" src="https://github.com/user-attachments/assets/52b7079d-77e4-4191-b90c-8d94b80b615e" /> <img width="270" height="585" alt="ios_2" src="https://github.com/user-attachments/assets/48643417-0fa6-43ed-b744-9c25873321d2" /> <img width="270" height="585" alt="ios_3" src="https://github.com/user-attachments/assets/f366319b-9856-4353-a7ea-d20f4ce7e13a" />

## 🧰 How to Run

1. Clone this repository:
```bash
git clone https://github.com/agachieusebiu/Crypto-Currency-Multiplatform-App.git
```

2. Open the project in **Android Studio Otter 3 Feature Drop | 2025.2.3**

3. Sync Gradle

4. Run:
   - **Android**: Run the Android configuration from Android Studio
   - **iOS**: Open `iosApp/iosApp.xcodeproj` in Xcode and run on simulator/device

## ✅ Tests

Shared UI tests live in `composeApp/src/commonTest` and validate:
- Submit button label changes per trade type
- Coin name displays correctly
- Error banner/message renders when present

## 🧑‍💻 Author

**Eusebiu-Gabriel Agachi**  
📍 Iași, Romania  
📧 agachi.eusebiu@gmail.com  
🔗 [LinkedIn](https://www.linkedin.com/in/eusebiu-agachi-1b02a7231/)

---

⭐ If you find this project useful, consider giving it a star!
