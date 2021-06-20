# Chapter 09 - Exploring the Initial Architecture on Google Compute Engine

# Additional Steps

### Make sure you run it on Java version 8

### For database connection:

- change the bind address to 0.0.0.0 in my.cnf file (in mysql-vm) [ /opt/bitnami/mysql/conf/my.cnf ]
- open up the 3306 port using ufw (in mysql-vm)
- change the host name in connection string to private ip of the mysql-vm (in tomcat-vm)

### For redis connection:

- change the bind address to 0.0.0.0 in redis.cnf file (in redis-vm) [ /opt/bitnami/redis/etc/redis.conf ]
- open up the 6379 port using ufw (in redis-vm)
- change the host name to private ip of the redis-vm (in tomcat-vm)
