import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.font_manager
from sklearn.model_selection import train_test_split
from sklearn import svm

hyperparameters = [[0.01, 0.15, 512], [0.03, 0.05, 512], [0.03, 0.06, 256], [0.03, 0.1, 64]] # nu, gamma, cost for complexity, cohesion, coupling, inheritance

for i in range(4):

    xx, yy = np.meshgrid(np.linspace(-5, 5, 500), np.linspace(-5, 5, 500))
    # Generate train data
    X = np.random.randn(1000, 2)
    # Choose your test size to split between training and testing sets:
    X_train, X_test, _, _ = train_test_split(X, X, test_size=0.25, random_state=42)


    # fit the model
    clf = svm.OneClassSVM(nu=hyperparameters[i][0], kernel="rbf", gamma=hyperparameters[i][1])
    clf.fit(X_train)
    y_pred_train = clf.predict(X_train)
    y_pred_test = clf.predict(X_test)
    n_error_train = y_pred_train[y_pred_train == -1].size
    n_error_test = y_pred_test[y_pred_test == -1].size
#
#     # plot the line, the points, and the nearest vectors to the plane
#     Z = clf.decision_function(np.c_[xx.ravel(), yy.ravel()])
#     Z = Z.reshape(xx.shape)
#
#     plt.title("Novelty Detection")
#     plt.contourf(xx, yy, Z, levels=np.linspace(Z.min(), 0, 7), cmap=plt.cm.PuBu)
#     a = plt.contour(xx, yy, Z, levels=[0], linewidths=2, colors='darkred')
#     plt.contourf(xx, yy, Z, levels=[0, Z.max()], colors='palevioletred')
#
#     s = 40
#     b1 = plt.scatter(X_train[:, 0], X_train[:, 1], c='white', s=s, edgecolors='k')
#     b2 = plt.scatter(X_test[:, 0], X_test[:, 1], c='blueviolet', s=s,
#                      edgecolors='k')
#     c = plt.scatter(X_outliers[:, 0], X_outliers[:, 1], c='gold', s=s,
#                     edgecolors='k')
#     plt.axis('tight')
#     plt.xlim((-5, 5))
#     plt.ylim((-5, 5))
#     plt.legend([a.collections[0], b1, b2, c],
#                ["learned frontier", "training observations",
#                 "new regular observations", "new abnormal observations"],
#                loc="upper left",
#                prop=matplotlib.font_manager.FontProperties(size=11))
#     plt.xlabel(
#         "error train: %d/200 ; errors novel regular: %d/40 ; "
#         "errors novel abnormal: %d/40"
#         % (n_error_train, n_error_test, n_error_outliers))
#     plt.show()