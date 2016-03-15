import math
#Python has a method called zipped that does practically the same
def parallelInter(list1, list2):
	result = [];
	for x in range(min(len(list1), len(list2))):
		result.append(list1[x])
		result.append(list2[x])
	if len(list1) > len(list2):
		for i in range(len(list2), len(list1)):
			result.append(list1[i])
	elif len(list2) > len(list1):
		for i in range(len(list1), len(list2)):
			result.append(list2[i])
	return result

#Method to calculate all the interleavings of two lists.
#Warning: This method is higly inneficient.
def allInter(list1, list2):
	#List to store the final result
	result = []
	#Number of combinations possible given the lengths of the lists
	numberOfCombinations = (math.factorial(len(list1) + len(list2))/(math.factorial(len(list1))*math.factorial(len(list2))))
	#List to store the indexes of the first list
	position = []

	n = (len(list1) + len(list2)) - 1

	#Adding the first element to the resulting list
	first = list(list1)
	for i in list2:
		first.append(i)
	result.append(first)

	#Create a list containing all the positions 
	for i in range(len(list1)):
		position.append(i)

	#Create all the possible intervalations of the two lists
	for x in range(numberOfCombinations):
		#Current tuple we are creating, this will be added to the resulting list
		currentTuple = []
		#check each of the positions in reverse, if the position is still valid, add one to it.
		k = len(position) - 1
		#correction factor
		fix = 0
		#where the magic happens.
		while k >= 0:
			if(position[k] < n - fix):
				position[k]+=1
				for i in range(k + 1, len(position)):
					position[i] = position[i - 1] + 1
				break
			fix = fix + 1
			k = k - 1

		#create the current tuple to be added to the resulting list.
		tempList1 = list(list1)
		tempList2 = list(list2)
		w = 0
		for current in range((len(list1) + len(list2))):
			if w < len(position) and current == position[w]:
				currentTuple.append(tempList1.pop(0))
				w = w + 1
			else:
				currentTuple.append(tempList2.pop(0))
		result.append(currentTuple)
	#return the result
	return result

something1 = parallelInter([1,2,3,4], [5,6,7,8,9])
print "Parallel interleaving"
print something1
print
print "All Interleaving"
something2 = allInter([1,2,3,4], [5,6,7,8,9])
for x in something2:
	print x
