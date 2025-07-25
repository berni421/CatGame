const mice = [];
let mouseImage;
let mouseSounds;
// let myFont;
// const fontSize = 12;
let miceMax;

function preload() {
  // print("start preload");
  mouseImage = loadImage("../images/mouse.jpg");
  soundFormats("ogg", "mp3");
  mouseSounds = loadSound("../sounds/mouse-sounds.mp3");
  // myFont = loadFont("../fonts/DejaVuSerif.ttf");
  // print("end preload");
}

function setup() {
  // print("start setup");
  createCanvas(windowWidth, windowHeight, WEBGL);
  miceMax = int(windowWidth / 256);
  if (miceMax < 2) {
    miceMax = 2;
  }
  for (let i = 0; i < miceMax; i++) {
    // print("try:", i)
    const mouse = new Mouse(mouseImage, i, 64);
    const o = overlaps(mouse);
    if (!o) {
      mice.push(mouse);
    } else {
      i--;
    }
  }
  frameRate(12);
  mouseSounds.loop();
  // print("end setup");
}

function draw() {
  background("black");
  for (let i = 0; i < mice.length; i++) {
    const mouse = mice[i];
    mouse.update();
    mouse.display();
    fixCollision(mouse);
  }
  // textFont(myFont);
  // textSize(fontSize)
  // // fill("black");
  // x = -width / 2;
  // text("width: " + width, x, 0);
  // text("height: " + height, x, fontSize);
  // text("mice: " + miceMax, x, fontSize * 2);
}

function fixCollision(mouse) {
  for (let i = 0; i < mice.length; i++) {
    if (mouse.name !== mice[i].name) {
      const o = overlaps(mouse);
      if (o) {
        // print("collision:", mouse.name, " and ", mice[i].name);
        // print("mouse", mouse);
        mouse.reverse();
        // print("mouse reversed", mouse);
        break;
      }
    }
  }
}

function overlaps(mouse) {
  const mX = mouse.x;
  const mY = mouse.y;
  const sr = mouse.sr;
  for (let i = 0; i < mice.length; i++) {
    if (mouse.name !== mice[i].name) {
      const iX = mice[i].x;
      const iY = mice[i].y;
      const clearX = (mX + sr < iX) || (iX + sr < mX);
      // const clearX = abs(mX - iX) > sr;
      const clearY = (mY + sr < iY) || (iY + sr < mY);
      // const clearY = abs(mY - iY) > sr;
      const o = (!clearX && !clearY);
      if (o) {
        // print("overlaps:", mouse.name, " and ", mice[i].name);
        return true;
      }
    }
  }
  return false;
}

function mousePressed() {
  noLoop();
  mouseSounds.stop();
}

function touchStarted() {
  mousePressed();
}

function mouseReleased() {
  loop();
  mouseSounds.loop();
}