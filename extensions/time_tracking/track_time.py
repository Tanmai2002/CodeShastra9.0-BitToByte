import time
import json
import sys
# print(time.ctime())
start=time.ctime()
time.sleep(60)

while True:
    end=time.ctime()
    data={'start':start, 'end':end}
    f=open('example.json', 'w')
    json.dump(data, f)
    f.close()
    time.sleep(121)
    # with open('example.json', 'w') as f:
    #     json.dump(data, f)
    # sys.exit(0)




