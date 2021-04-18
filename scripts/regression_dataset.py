import os
import pandas as pd

# os.chdir("..")
dir = '../src/main/resources/dataset'
print(os.getcwd() + dir)


package_rows = []
for dir, subdirs, files in os.walk(dir):
    for file in files:
        if (file.endswith("class.csv")):
            filepath = os.path.join(os.path.abspath(dir), file)
            df = pd.read_csv(filepath)
            df = df.dropna()
            if df.shape[0] > 1:
                packages = {}
                package_rows.append(
                    {
                        'package_name': filepath.split('/')[-2],
                        'release': filepath.split('/')[-3],
                        'cbo': df['cbo'].mean(),
                        'nosi': df['nosi'].mean(),
                        'loc': df['loc'].mean(),
                        'wmc': df['wmc'].mean(),
                        'rfc': df['rfc'].mean(),
                        'lcom': df['lcom'].mean(),
                        'tcc': df['tcc'].mean(),
                        'lcc': df['lcc'].mean(),
                        'dit': df['dit'].mean(),
                    })

import csv
with open('regression_dataset.csv', 'w') as f:
    fieldnames = ['package_name', 'release', 'cbo', 'nosi', 'loc', 'wmc', 'rfc', 'lcom', 'tcc', 'lcc',  'dit']
    writer = csv.DictWriter(f, fieldnames=fieldnames)
    writer.writeheader()
    writer.writerows(d for d in package_rows)