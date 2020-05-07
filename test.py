import requests
import random
import datetime
from dateutil.relativedelta import *

f = open('output.txt', 'w')
customers = 2
transactions = 10
endDate = datetime.date(2020, 5, 6)

def dateAsString(transactionDate):
    transactionMonth = transactionDate.month
    transactionDay = transactionDate.day
    if(transactionMonth < 10):
        transactionMonth = "0" + str(transactionMonth)
    else:
        transactionMonth = str(transactionMonth)
    if(transactionDay < 10):
        transactionDay = "0" + str(transactionDay)
    else:
        transactionDay = str(transactionDay)
    return str(transactionDate.year) + "-" + str(transactionMonth) + "-" + str(transactionDay)

def randDate():
    month = random.randint(1,5)
    #year = random.randint(2019,2020)
    year = 2020
    if(year == 2020 and month == 2):
        day = random.randint(1,29)
    elif(month == 2):
        day = random.randint(1,28)
    elif(month == 4 or month == 6 or month == 9 or month == 11):
        day = random.randint(1,30)
    else:
        day = random.randint(1,31)
    date = datetime.date(year, month, day)
    return date

oneMonthBefore = endDate + relativedelta(months=-1)
custPointsOneMonth = {}
twoMonthsBefore = endDate + relativedelta(months=-2)
custPointsTwoMonths = {}
threeMonthsBefore = endDate + relativedelta(months=-3)
custPointsThreeMonths = {}
custPointsTotalMonths = {}

for x in range(0,transactions):
    transactionDate = randDate();
    transactionAmount = random.randint(1,1000)
    transactionDateAsString = dateAsString(transactionDate)
    points = 0
    if(transactionAmount > 100):
        points = 2 * (transactionAmount - 100) + 50
    elif(transactionAmount > 50):
        points = transactionAmount - 50
    customerID = random.randint(1,customers)
    f.write("customerID: " + str(customerID) + " transactionAmount: " + str(transactionAmount) + " points: " + str(points) + " date: " + transactionDateAsString + "\n")
    if (threeMonthsBefore < transactionDate and transactionDate < endDate and transactionDate <= twoMonthsBefore):
        if(str(customerID) in custPointsThreeMonths):
            custPointsThreeMonths[str(customerID)] += points
        else:
            custPointsThreeMonths[str(customerID)] = points
        if(str(customerID) in custPointsTotalMonths):
            custPointsTotalMonths[str(customerID)] += points
        else:
            custPointsTotalMonths[str(customerID)] = points
    elif (twoMonthsBefore < transactionDate and transactionDate < endDate and transactionDate <= oneMonthBefore):
        if(str(customerID) in custPointsTwoMonths):
            custPointsTwoMonths[str(customerID)] += points
        else:
            custPointsTwoMonths[str(customerID)] = points
        if(str(customerID) in custPointsTotalMonths):
            custPointsTotalMonths[str(customerID)] += points
        else:
            custPointsTotalMonths[str(customerID)] = points
    elif (oneMonthBefore < transactionDate and transactionDate <= endDate):
        if(str(customerID) in custPointsOneMonth):
            custPointsOneMonth[str(customerID)] += points
        else:
            custPointsOneMonth[str(customerID)] = points
        if(str(customerID) in custPointsTotalMonths):
            custPointsTotalMonths[str(customerID)] += points
        else:
            custPointsTotalMonths[str(customerID)] = points    
          
    data = '''{''' + '''"customerID": ''' + str(customerID) + ''',\n''' + '''"transactionAmount": ''' + str(transactionAmount) + ''',\n''' + '''"transactionDate": "''' + transactionDateAsString + '''"\n''' + '''}'''
    response = requests.post("http://localhost:8080/transaction/", data=data, headers={'content-type': 'application/json'})
    print(data)
    
f.write("1st month\n")
for x in custPointsThreeMonths:
    f.write("customer " + str(x) + " earned " + str(custPointsThreeMonths[x]) + " points\n")

f.write("2nd month\n")
for x in custPointsTwoMonths:
    f.write("customer " + str(x) + " earned " + str(custPointsTwoMonths[x]) + " points\n")

f.write("3rd month\n")
for x in custPointsOneMonth:
    f.write("customer " + str(x) + " earned " + str(custPointsOneMonth[x]) + " points\n")
f.write("Total\n")
for x in custPointsTotalMonths:
    f.write("customer " + str(x) + " earned " + str(custPointsTotalMonths[x]) + " points\n")
