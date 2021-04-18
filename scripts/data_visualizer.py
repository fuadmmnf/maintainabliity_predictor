import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

np.random.seed(1234)

import seaborn as sns
sns.set_theme(style="whitegrid")

df = pd.read_csv('regression_dataset.csv')

a = sns.boxplot(data=df.iloc[:, 2:], orient="h", palette="Set2")
plt.show()
a.get_figure().savefig('boxplot.png')
for column in df.columns[2: ]:
    a = sns.boxplot(data=df[column], orient="h", palette="Set2").set(title='lalala', xlabel='its x_label', ylabel=column)
    plt.show()
