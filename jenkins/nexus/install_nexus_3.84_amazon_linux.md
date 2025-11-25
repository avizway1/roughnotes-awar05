# Install Nexus Repository OSS 3.84.0-03 on Amazon Linux 2023 (Simple Guide)

This guide uses the **working URL you provided** and keeps the steps minimal.

---

## 1) Update system & install prerequisites
```bash
sudo dnf update -y
sudo dnf install -y java-17-amazon-corretto wget tar
```

---

## 2) Create a dedicated 'nexus' user (no home dir, no shell)
```bash
sudo useradd -r -M -d /opt/nexus -s /sbin/nologin nexus
```

> Using `-M` avoids creating `/opt/nexus` as a directory so we can point it to the Nexus folder via a symlink.

---

## 3) Download and unpack Nexus 3.84.0-03
```bash
cd /opt
sudo wget -O nexus-3.84.0-03.tar.gz   https://download.sonatype.com/nexus/3/nexus-3.84.0-03-linux-x86_64.tar.gz

sudo tar -xzf nexus-3.84.0-03.tar.gz
sudo ln -sfn /opt/nexus-3.84.0-03 /opt/nexus
sudo mkdir -p /opt/sonatype-work
sudo chown -R nexus:nexus /opt/nexus /opt/nexus-3.84.0-03 /opt/sonatype-work
```

---

## 4) Run Nexus as the 'nexus' user
```bash
echo 'run_as_user="nexus"' | sudo tee /opt/nexus/bin/nexus.rc
```

---

## 5) Systemd service
```bash
sudo tee /etc/systemd/system/nexus.service > /dev/null <<'EOF'
[Unit]
Description=Sonatype Nexus Repository Manager
After=network.target

[Service]
Type=forking
User=nexus
Group=nexus
LimitNOFILE=65536
Environment=INSTALL4J_JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
ExecStart=/opt/nexus/bin/nexus start
ExecStop=/opt/nexus/bin/nexus stop
Restart=on-abort
TimeoutSec=600

[Install]
WantedBy=multi-user.target
EOF
```

---

## 6) Enable & start
```bash
sudo systemctl daemon-reload
sudo systemctl enable --now nexus
sudo systemctl status nexus -l
```

---

## 7) Access Nexus
- URL: `http://<EC2-Public-IP>:8081`
- Default admin password file:
  ```
cat /opt/sonatype-work/nexus3/admin.password
  ```

> Remember to **open TCP 8081** in your EC2 Security Group inbound rules.
