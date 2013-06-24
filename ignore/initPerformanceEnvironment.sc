(
  var m, mBounds;
 
  s.quit;
  
  /*s.options.inDevice = "PreSonus FIREPOD (2112)";*/
  /*s.options.inDevice = "SF + 1818";*/
  /*s.options.inDevice = "SF + Firepod";*/
  /*s.options.inDevice = "AudioBox 1818 VSL ";*/
  /*s.options.inDevice = "Soundflower (64ch)";*/

  /*s.options.outDevice = "Soundflower (64ch)";*/
  s.options.inDevice = "JackRouter";
  s.options.outDevice = "JackRouter";
  s.options.numOutputBusChannels = 32;
  /*s.options.numInputBusChannels = 32;*/
  /*s.options.maxNodes = 2048;
  s.options.maxSynthDefs = 2048;
  s.options.memSize = 16384;
  s.options.numAudioBusChannels = 256;
  s.options.numBuffers = 2048;
  s.options.numControlBusChannels = 8192;
  s.options.numRGens = 128;
  s.options.numWireBufs = 128;
  s.options.numPrivateAudioBusChannels = 256;*/

  //s.options.sampleRate = 48000;
  s.options.blockSize = 8;
  /*s.options.hardwareBufferSize = 128;*/
  s.boot();
  m = s.meter();

  // move level meter to bottom right of screen
  mBounds = m.window.bounds;
  /*mBounds.left = 1680;
  mBounds.top = 1000;*/
  mBounds.left = 1440;
  mBounds.top = 900;
  
  m.window.setTopLeftBounds(mBounds);
  
  GUI.qt;
  Instr.dir = "/Users/colin/Projects/cs-supercollider-lib/Instr/";
  Instr.loadAll();

  s.doWhenBooted({

    MIDIClient.init();
    MIDIPort.init();
    MIDIIn.connectAll();
    
    "----------------------------------------".postln;
    "* Starting Performance Environment...".postln;
    "----------------------------------------".postln;
    
    ~performanceEnvironment = PerformanceEnvironment.new();
  });

)
