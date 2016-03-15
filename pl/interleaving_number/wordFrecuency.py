import string
from operator import attrgetter, itemgetter
#Open the file
f = open('input.txt', 'r')
print delimiter
#This is a class to hold the times a word appeared and in which lines did it appear.
class Words:
	def __init__(self, name, frecuency, appeared):
		self.name = name
		self.frecuency = frecuency
		self.appeared = appeared
#HashMap (dictionary) to keep all the values of each word
theWords = {}
lineCount = 0
lines = f.readlines()
for line in lines:
	#By using the translate method I was able to eliminate the punctuation marks
	#But the periods are still registered.
	tempString = line.translate(None, string.punctuation)
	allTheWords = tempString.split()
	lineCount+= 1
	for word in allTheWords:
		word = word.lower()
		if word in theWords:
			theWords[word].frecuency+=1
			theWords[word].appeared+= ", " + str(lineCount)
		else: 
			theWords[word] = Words(word, 1, str(lineCount))
tempList = []
#place all the information as a tuple in a list
for eachThing in theWords.values():
	tempList.append((eachThing.frecuency, eachThing.name, eachThing.appeared))
#Keeping in mind the parameters, sort the list with the most frequent at the top, then lexicographically.
tempList.sort(key=lambda k: (-k[0], k[1]))
#print the results in the command line
for x in tempList:
	print x