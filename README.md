Grader - prototype
=================================

###A simple single-problem grader ###
Created using Play Framework and Docker.

Minimum Requirement
-------------------
* Ubuntu 12.04
* Oracle Java 8 (<http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>)
* Docker 1.6.2 (<https://docs.docker.com/installation/ubuntulinux/>)

How to use
----------------
* Pull the container image using `docker pull paradisecatz/grader-container`
* Run the server using `activator run` command inside main directory
* Go to localhost:9000

Testcase directory can be found in `.../testcase`.

If you're giving template for submission, please include makefile with 'compile' rule for compiling submission and 'run' rule for executing submission. Put the template file in `.../template'.

Configuration
----------------
* Time limit and memory limit of problem can be changed in `application.conf`.
* List of file that must be overwrite when using template submission can be configure in `application.conf` on `mandatory.file.list` variable.
