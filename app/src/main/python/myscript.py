import numpy as np

def postprocessing(predictions):
    # predictions = [i[0] for i in predictions]
    predictions = np.array(predictions)

    book_id = (-predictions).argsort()[:25]
    book_id = book_id.tolist()

    return book_id

def unique(list1):
    list1 = list(list1)
    list2 = list(set(list1))
    return list2

def getToIndex(list):
    predictions = np.array(list)

    book_id = (-predictions).argsort()[:25]
    book_id = book_id.tolist()
    return book_id
