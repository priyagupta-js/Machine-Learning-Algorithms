#!/usr/bin/env python
# coding: utf-8

# In[11]:


import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
get_ipython().run_line_magic('matplotlib', 'inline')


# In[12]:


df = pd.read_csv('/home/student/Downloads/iris.csv')


# In[13]:


df


# In[14]:


df.head()


# In[15]:


df.tail()


# In[16]:


from sklearn.tree import DecisionTreeClassifier
dtclassifier = DecisionTreeClassifier(criterion ='entropy')
a =df.iloc[:,:-1]
b =df.iloc[:,-1]
dtclassifier.fit(a,b)


# In[17]:


from sklearn import tree
plt.figure(figsize=(15,10))
tree.plot_tree(dtclassifier,filled = True)


# In[18]:


#using gini


# In[20]:


from sklearn.tree import DecisionTreeClassifier
dtclassifier = DecisionTreeClassifier(criterion ='gini')
a1 =df.iloc[:,:-1]
b1 =df.iloc[:,-1]
dtclassifier.fit(a1,b1)


plt.figure(figsize=(15,10))
tree.plot_tree(dtclassifier,filled = True)


# In[21]:


from sklearn.tree import DecisionTreeClassifier
dtclassifier = DecisionTreeClassifier(criterion ='gini',max_depth=2)
a1 =df.iloc[:,:-1]
b1 =df.iloc[:,-1]
dtclassifier.fit(a1,b1)


plt.figure(figsize=(15,10))
tree.plot_tree(dtclassifier,filled = True)


# In[22]:


from sklearn.model_selection import GridSearchCV
a2 =df.iloc[:,:-1]
b2 =df.iloc[:,-1]


# In[26]:


param_grid = {
"criterion":["gini","entropy"],
"maxdepth":[2,3,4,5],
"mindepth":[2,3,4,5],
"min_sample_split":[2,3,4,5],
"min_sample_leaf":[1,2,3,4],
}


# In[29]:


grid_search =GridSearchCV(dtclassifier ,param_grid ,cv=5)


# In[30]:


grid_search.fit(a2,b2)


# In[28]:


best_params =grid_search.best_params_
best_params


# In[ ]:




