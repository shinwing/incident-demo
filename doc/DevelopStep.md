# Incident Management Development Steps
## 1. Setup development environment
### 1.1. Install Java 17
- https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
```angular2html
java -version 
java version "17.0.11" 2024-04-16 LTS
Java(TM) SE Runtime Environment (build 17.0.11+7-LTS-207)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.11+7-LTS-207, mixed mode, sharing)
```

### 1.2. Install idea
### 1.3. Install wrk
### 1.4. Install React and Axios
```angular2html
brew install node  
node -v                                                                                                                                                                
npm -v22.8.0
npm -v
10.8.2

npx create-react-app incident-management
Need to install the following packages:
create-react-app@5.0.1
Ok to proceed? (y) y

cd incident-management
npm install axios
```

## 2. Develop
### 2.1. Create an empty springboot project
- https://start.spring.io/

### 2.2. Implement the Incident management code 

### 2.3. Database access url 
- http://localhost:8080/h2-console

### 2.4. Implement the frontend code

## 3. Complete the unit test and e2e test 

## 4. Build and run the project
```angular2html
docker build -t incident-management:1.0.0 .
docker build -t incident-management-front:1.0.0 -f frontend/incident-management/dockerfile .
```