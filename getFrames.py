import cv2

def getFrames(path_to_vid, frameCount):
    cap = cv2.VideoCapture(path_to_vid)
    i = 0
    while cap.isOpened():
        ret, frame = cap.read()
        if ret == False:
            break
        if i != frameCount:
            i+= 1
            cv2.imwrite("frame"+str(i)+".jpg",frame)
    cap.release()

getFrames("test.mp4",5)