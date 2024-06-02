# PokeViewr
PokeViewr is an Android application that offers users a seamless experience in exploring Pokémon. Built with modern technologies and following best practices, PokeViewr utilizes the MVVM (Model-View-ViewModel) architectural pattern, Retrofit for network requests, Hilt for dependency injection, coroutines for asynchronous programming, Room for local data storage, and Google Material Design principles to deliver a smooth and user-friendly interface.

## Features

### Home Tab
- **Pokémon List:** Easily browse through the list of Pokémon with smooth scrolling
- **Pokémon Details:** Dive into detailed information about each Pokémon, including name, weight, height, ID, about, stats, types, abilities, and moves.
- **Pokémon Capture:** Try catching Pokémon with a 50% chance.

### MyPokemon Tab
- **Captured Pokémon:** Access a list of captured Pokémon.
- **Fibonacci Number:** Each captured Pokémon's name is decorated with a unique Fibonacci number based on its species.
- **Swipe Actions:** Edit or release Pokémon with simple swipe gestures.

### Shared Features
- **Bottom Navigation:**  Switch between the Home and MyPokemon tabs.
- **User-friendly Interface:** Enjoy a user-friendly interface designed for smooth navigation and interaction.

## Installation
1. Clone the repository: `git clone https://github.com/tobiasrandy/PokeViewr.git`
2. Open the project in Android Studio.
3. Build and run the app on your device or emulator.

## Usage
- Launch the PokeViewr app and navigate to the desired tab.
- Browse through the extensive list of Pokémon or captured Pokémon with smooth scrolling.
- Tap on a Pokémon to view its detailed information, including name, weight, height, ID, about, stats, types, abilities, and moves.
- Try your luck at catching Pokémon by clicking a Pokéball icon on the top right corner, with a 50% chance, and an additional 10% chance to catch a shiny Pokémon.
- Swipe on a captured Pokémon to edit its name, or swipe left to release the Pokémon. A random number will be generated, and if the number is not a prime number, the release will fail, and vice versa.
- Switch between the Home and MyPokemon tabs using the bottom navigation.

## Contributing
Contributions are welcome! Please fork the repository and create a pull request with your changes.

## Contact
For any questions or feedback, please contact [Tobias Randy](mailto:randyvarianchou@gmail.com).

## License
This project is licensed under the [MIT License](LICENSE).
