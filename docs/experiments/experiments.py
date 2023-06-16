import matplotlib.pyplot as plt

# 定义数据点
number_of_clients = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
completion_time = [1600, 1450, 1350, 1150, 1176, 960, 900, 850, 700, 450]

# 创建折线图
plt.plot(number_of_clients, completion_time, marker='o')

# 设置轴标签和标题
plt.xlabel('Number of Clients')
plt.ylabel('Completion Time (in seconds)')
plt.title('Completion Time')

# 设置轴的刻度范围和间隔
plt.xticks(number_of_clients)
plt.yticks(range(0, 1800, 400))

# 显示图形
plt.show()

