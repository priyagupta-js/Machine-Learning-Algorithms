#!/usr/bin/env python
# coding: utf-8

# In[1]:


import numpy as np
import pandas as pd
import matplotlib as plt


# In[5]:


df = pd.read_csv('/home/student/Downloads/iris.csv')


# In[6]:


df


# In[7]:


x =df.iloc[:,:-1].values
y =df.iloc[:,4].values


# In[8]:


x


# In[11]:


from sklearn.model_selection import train_test_split
x_train ,x_test,y_train,y_test = train_test_split(x,y,test_size =0.20)


# In[16]:


from sklearn.preprocessing import StandardScaler
scaler = StandardScaler()
scaler.fit(x_train)
x_train = scaler.transform(x_train)
x_test = scaler.transform(x_test)


# In[21]:


from sklearn.neighbors import KNeighborsClassifier
classifier = KNeighborsClassifier(n_neighbors=5)
classifier.fit(x_train,y_train)


# In[23]:


y_pred = classifier.predict(x_test)


# In[24]:


from sklearn.metrics import classification_report,confusion_matrix
print(classification_report(y_test,y_pred))
print(confusion_matrix(y_test,y_pred))


# In[ ]:




