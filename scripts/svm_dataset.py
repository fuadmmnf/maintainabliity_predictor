import csv

import matplotlib.pyplot as plt
from scipy import stats
import pandas as pd


# plt.scatter(x, y)
# plt.plot(x, mymodel)
# plt.show()
# complexity- wmc, nosi, loc
# coupling- cbo, rfc
# cohesion- lcom, tcc, lcc
# inheritance- dit


def getUniqueValueList(dataList):
    uniqueList = []

    for x in dataList:
        # check if exists in unique_list or not
        if x not in uniqueList:
            uniqueList.append(x)

    return uniqueList


def getPackageGroupWithUpdate(dataFrame, uniquePackages):
    packageGroup = {}
    # print(len(uniquePackages))
    for package in uniquePackages:
        packageDataFrame = dataFrame[dataFrame['package_name'] == package]
        packageGroup[package] = packageDataFrame

    return packageGroup


def getRegressionAnalysis(x, y):
    print(len(x), y)
    slope, intercept, r, p, std_err = stats.linregress(x, y)
    return (slope, intercept)


def getXList(length):
    x = []
    for i in range(0, length):
        x.append(i)
    return x


def writeInCSV(filename, fields, rowList):
    with open(filename, 'w', newline='') as csvfile:
        # creating a csv writer object
        csvwriter = csv.writer(csvfile)

        # writing the fields
        csvwriter.writerow(fields)

        # writing the data rows
        for row in rowList:
            csvwriter.writerow(row)


def writeInComplexityCSVFile(rowList):
    fields = ['package_name', 'wmc_slope', 'wmc_intercept', 'nosi_slope', 'nosi_intercept', 'loc_slope',
              'loc_intercept']
    outputCSV = r'dataset/complexity_dataset.csv'
    writeInCSV(outputCSV, fields, rowList)


def writeInCouplingCSVFile(rowList):
    fields = ['package_name', 'cbo_slope', 'cbo_intercept', 'rfc_slope', 'rfc_intercept']
    outputCSV = r'dataset/coupling_dataset.csv'
    writeInCSV(outputCSV, fields, rowList)


def writeInCohesionCSVFile(rowList):
    fields = ['package_name', 'lcom_slope', 'lcom_intercept', 'tcc_slope', 'tcc_intercept', 'lcc_slope',
              'lcc_intercept']
    outputCSV = r'dataset/cohesion_dataset.csv'
    writeInCSV(outputCSV, fields, rowList)


def writeInInheritanceCSVFile(rowList):
    fields = ['package_name', 'dit_slope', 'dit_intercept']
    outputCSV = r'dataset/inheritance_dataset.csv'
    writeInCSV(outputCSV, fields, rowList)


def datasetGenerator(datasetPath):
    dataFrame = pd.read_csv(datasetPath)
    packageObject = dataFrame['package_name']
    packagesList = packageObject.values.tolist()
    uniquePackages = getUniqueValueList(packagesList)

    packageGroup = getPackageGroupWithUpdate(dataFrame, uniquePackages)

    # outputPath = r'dataset/dataset.csv'
    # 
    # print(len(uniquePackages))
    # # print(packageGroup[uniquePackages[10]]['release'])
    # length = 0
    # maxLength = 0
    # packageName = ''
    # for package in uniquePackages:
    #     l = len(packageGroup[package].values)
    #     length += l
    #     if maxLength < l:
    #         maxLength = l
    #         packageName = package
    # 
    # packageGroup[packageName].to_csv(outputPath, index=True)
    # print(packageGroup[packageName][''])
    # print(maxLength)

    complexityRows = []
    couplingRows = []
    cohesionRows = []
    inheritanceRows = []

    for package in uniquePackages:
        df = packageGroup[package]

        X = getXList(len(df.values))
        # int_list = [int(i) for i in df['wmc'].values.tolist()]

        # complexity
        m1, c1 = getRegressionAnalysis(X, [float(i) for i in df['wmc'].values.tolist()])
        m2, c2 = getRegressionAnalysis(X, [float(i) for i in df['nosi'].values.tolist()])
        m3, c3 = getRegressionAnalysis(X, [float(i) for i in df['loc'].values.tolist()])

        complexityRow = [package, m1, c1, m2, c2, m3, c3]
        complexityRows.append(complexityRow)

        # coupling
        m1, c1 = getRegressionAnalysis(X, [float(i) for i in df['cbo'].values.tolist()])
        m2, c2 = getRegressionAnalysis(X, [float(i) for i in df['rfc'].values.tolist()])

        couplingRow = [package, m1, c1, m2, c2]
        couplingRows.append(couplingRow)

        # cohesion
        m1, c1 = getRegressionAnalysis(X, [float(i) for i in df['lcom'].values.tolist()])
        m2, c2 = getRegressionAnalysis(X, [float(i) for i in df['tcc'].values.tolist()])
        m3, c3 = getRegressionAnalysis(X, [float(i) for i in df['lcc'].values.tolist()])

        cohesionRow = [package, m1, c1, m2, c2, m3, c3]
        cohesionRows.append(cohesionRow)

        # inheritance
        m1, c1 = getRegressionAnalysis(X, [int(i) for i in df['dit'].values.tolist()])

        inheritanceRow = [package, m1, c1]
        inheritanceRows.append(inheritanceRow)

    # writeInComplexityCSVFile(complexityRows)
    # writeInCouplingCSVFile(couplingRows)
    # writeInCohesionCSVFile(cohesionRows)
    # writeInInheritanceCSVFile(inheritanceRows)


datasetGenerator('dataset/regression_dataset.csv')
