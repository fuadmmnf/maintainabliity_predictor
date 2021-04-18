import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.font_manager
from sklearn.model_selection import train_test_split
from sklearn import svm

hyperparameters = [[0.01, 0.15, 512], [0.03, 0.05, 512], [0.03, 0.06, 256], [0.03, 0.1, 64]] # nu, gamma, cost for complexity, cohesion, coupling, inheritance
dataset_files = ['dataset/cohesion_dataset.csv', 'dataset/complexity_dataset.csv', 'dataset/coupling_dataset.csv' , 'dataset/inheritance_dataset.csv'] #cohesion has negative relation, placed first for inverse slope logic

for i in range(4):

    xx, yy = np.meshgrid(np.linspace(-5, 5, 500), np.linspace(-5, 5, 500))
    # Generate train data
    # X = np.random.randn(1000, 2)
    df = pd.read_csv(dataset_files[i]).dropna()

    # Choose your test size to split between training and testing sets:
    X_train, X_test, _, _ = train_test_split(df.iloc[:, 1:], df.iloc[:, 1:], test_size=0.25, random_state=42)


    # fit the model
    clf = svm.OneClassSVM(nu=hyperparameters[i][0], kernel="rbf", gamma=hyperparameters[i][1])
    clf.fit(X_train)
    y_pred_train = clf.predict(X_train)
    y_pred_test = clf.predict(X_test)
    n_error_train = y_pred_train[y_pred_train == -1].size
    n_error_test = y_pred_test[y_pred_test == -1].size

    print("Maintainability Characteristic: ", dataset_files[i].split('/')[1].split('_')[0])
    print("#Error pred train:", n_error_train, "#Pred train:", len(y_pred_train))
    print("#Error pred test:", n_error_test, "#Pred test:", len(y_pred_test))
    print('\n\n')
    # plot the line, the points, and the nearest vectors to the plane
    # Z = clf.decision_function(np.c_[xx.ravel(), yy.ravel()])
    # Z = Z.reshape(xx.shape)
    #
    # plt.title("Novelty Detection")
    # plt.contourf(xx, yy, Z, levels=np.linspace(Z.min(), 0, 7), cmap=plt.cm.PuBu)
    # a = plt.contour(xx, yy, Z, levels=[0], linewidths=2, colors='darkred')
    # plt.contourf(xx, yy, Z, levels=[0, Z.max()], colors='palevioletred')
    #
    # s = 40
    # b1 = plt.scatter(X_train[:, 0], X_train[:, 1], c='white', s=s, edgecolors='k')
    # b2 = plt.scatter(X_test[:, 0], X_test[:, 1], c='blueviolet', s=s,
    #                  edgecolors='k')
    #
    # plt.axis('tight')
    # plt.xlim((-5, 5))
    # plt.ylim((-5, 5))
    # plt.legend([a.collections[0], b1, b2],
    #            ["learned frontier", "training observations",
    #             "new regular observations"],
    #            loc="upper left",
    #            prop=matplotlib.font_manager.FontProperties(size=11))
    # plt.xlabel(
    #     "error train: %d/%d ; errors novel regular: %d/%d ; "
    #
    #     % (n_error_train, len(y_pred_train), n_error_test, len(y_pred_test)))
    # plt.show()