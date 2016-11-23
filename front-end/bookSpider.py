__author__='shou'
# _*_ coding:utf-8 -*_

import urllib.request
import re

#抓取书籍信息
class Spider:

	def __init__(self):
	    self.siteURL='http://bang.dangdang.com/books/bestsellers/01.52.00.00.00.00-recent7-0-0-1-'
	    self.file1 = None
	    self.file2 = None
	    self.isbn = 1000000001
	    self.publisherID = 2001

	def setFileTitle(self,title):
		self.file1 = open(title + ".txt","w+", encoding='utf8')

	def getContents(self, pageNum):
		url= self.siteURL + str(pageNum)
		print(url)
		request= urllib.request.Request(url)
		response= urllib.request.urlopen(request)
		content= response.read().decode('gbk')
		pattern = re.compile('<li>.*?<div class="list_num ">(.*?)</div>.*?<div class="name"><a href=.*? title="(.*?)">.*?</a></div>.*?<div class="publisher_info"><a href=.*?>(.*?)</a>.*?</div>.*?<div class="publisher_info"><span>.*?<a href=.*?>(.*?)</a></div>.*?<div class="price">.*?<span class="price_r">&yen;(.*?)\..*?</span>.*?</li>',re.S)
		items = re.findall(pattern, content)
		books = []
		for item in items:
			books.append("'"+ str(self.isbn) + "','" + item[2] + "','','" + item[1] + "','" + item[4] + "'#" + item[3]+ "\n")
			self.isbn+=1
		return books

	def writeData(self, books):
		for item in books:
			self.file1.write(item)

	def savePageInfo(self,pageIndex):
		books = self.getContents(pageIndex)
		self.writeData(books)

	def saveBookInfo(self, start, end):
		self.setFileTitle("books")
		for i in range(start,end+1):
			print (u"读取第",i,u"页数据")
			self.savePageInfo(i)


spider=Spider()
spider.saveBookInfo(1,8)


