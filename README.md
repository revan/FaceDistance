#FaceDistance

Android demo using OpenCV to determine face distance and orientation.

Circle drawn about detected face. Radius of circle provides a relative measure of distance to person.
![alt text](https://github.com/revansopher/FaceDistance/raw/master/pic1.png "pic1")

Facial detection algorithm fails on rotated faces, so failure can be interpreted as incorrect rotation.
![alt text](https://github.com/revansopher/FaceDistance/raw/master/pic2.png "pic2")

Note: Demo runs very slowly (~3 FPS) as a solitary streaming task, so streaming behind a game is not feasible. I lowered image quality to improve speed; lowering more will yield faster results, but game implementation should probably rely on periodic snapshots rather than a stream.