# 🚀 Deployment of Spring Boot Backend to Amazon EC2

This project demonstrates automated deployment of a Spring Boot backend to an Amazon EC2 instance using GitHub Actions.

## ⚙️ GitHub Actions Workflow

The workflow runs on every push to the master branch. It connects to your EC2 instance via SSH, builds the backend JAR, and restarts the service.

### `.github/workflows/deploy.yml`

```yaml
name: Deployment of SpringBoot

on:
    push:
        branches:
            - master

jobs:
    deploy:
        runs-on: ubuntu-latest

        steps:
            - name: Checkout Code
                uses: actions/checkout@v4

            - name: Deploy via SSH
                uses: appleboy/ssh-action@v1.0.3
                with:
                    host: ${{ secrets.SSH_IP_EC2 }}
                    username: ec2-user
                    key: ${{ secrets.SSH_KEY_EC2 }}
                    port: 22
                    script: |
                        cd /home/ec2-user/ExampleSpringBoot
                        git pull
                        mvn package -DskipTests
                        sudo systemctl stop exampleBackend || true
                        sudo cp ./target/exampleBackend-0.0.1.jar /opt/exampleBackend/
                        sudo systemctl start exampleBackend
```

## 🔑 Required Secrets

Add the following secrets in your GitHub repository settings:

- **SSH_IP_EC2** → Public IP of your EC2 instance
- **SSH_KEY_EC2** → Private SSH key for your EC2 instance

## 📂 Deployment Steps

### 1. Launch an EC2 Instance

- Choose Amazon Linux 2 or Amazon Linux 2023
- Select a t2.micro or larger instance depending on your workload
- Attach a key pair for SSH access

### 2. Configure Security Groups

- Allow SSH (port 22) from GitHub Actions (or your IP)
- Allow TCP port 9091 (or whichever port your Spring Boot app runs on)
- Optionally, allow HTTP (port 80) if you plan to proxy traffic via Nginx

### 3. Install Dependencies on EC2

```bash
sudo dnf install java-17-amazon-corretto -y
sudo dnf install git -y
sudo dnf install maven -y
```

### 4. Clone Your Project

```bash
cd /home/ec2-user
git clone https://github.com/<your-repo>.git ExampleSpringBoot
```

### 5. Setup Systemd Service

Create `/etc/systemd/system/exampleBackend.service`:

```ini
[Unit]
Description=Spring Boot Example Backend
After=network.target

[Service]
User=ec2-user
ExecStart=/usr/bin/java -jar /opt/exampleBackend/exampleBackend-0.0.1.jar
Restart=always

[Install]
WantedBy=multi-user.target
```

Enable and start:

```bash
sudo systemctl enable exampleBackend
sudo systemctl start exampleBackend
```

### 6. Push Code to Master

Whenever you push changes to the master branch:

- GitHub Actions connects to EC2
- Pulls the latest code
- Builds the JAR with Maven
- Copies the JAR to `/opt/exampleBackend/`
- Restarts the backend service

## ✅ Result

Your Spring Boot backend will automatically redeploy on EC2 whenever you push to master. The app will be accessible at:

```
http://<EC2-Public-IP>:9091
```
