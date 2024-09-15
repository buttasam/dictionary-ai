# Dictionary AI

## Description

Dictionary AI is a multi-language translator powered by ChatGPT. It offers AI-driven translations, user authentication, and the ability to save favorite translations. The application is built with a [Helidon 4](https://helidon.io/) (Java) backend, [Nuxt3/Vue](https://nuxt.com/) frontend styled with [Tailwind CSS](https://tailwindcss.com/), and uses [PostgreSQL](https://postgresql.org/) for data storage.

## Future Development

The development is still in progress. Additional features are planned for future implementation, which will expand Dictionary AI's capabilities and enhance the user experience.

## Table of Contents
- [Installation](#installation)
  - [Backend](#backend)
  - [Frontend](#frontend)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Installation

### Backend

1. Navigate to the backend directory:
   ```
   cd backend
   ```

2. Ensure you have Java 21+ installed.

3. Build the project using Maven:
   ```
   mvn clean package
   ```

4. Export your OpenAI secret key:
   ```
   export OPENAI_TOKEN=<your-secret-key>
   ```

5. Create a new PostgreSQL database and run the init script:
   ```
   psql -d your_database_name -f backend/init.sql
   ```

6. Update database credentials in `application.yaml`.

7. Run the JAR file:
   ```
   java -jar target/dictionary-ai-backend.jar
   ```
   Or use [helidon-cli](https://helidon.io/docs/v4/about/cli):
   ```
   helidon dev
   ```

The backend will be running on http://localhost:8080.

### Frontend

1. Navigate to the frontend directory:
   ```
   cd frontend
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. Run the development server:
   ```
   npm run dev
   ```

The frontend will be running on http://localhost:3000.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.