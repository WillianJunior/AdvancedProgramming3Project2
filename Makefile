all:
	javac -Xlint -deprecation -Werror MyConcurrent*.java CrawlerThread.java HarvestThread.java includeCrawler.java 

clean:
	-rm	*.class
