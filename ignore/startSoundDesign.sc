({
  MIDIClient.init;
  MIDIIn.connectAll;
  API.mountDuplexOSC();
  s.options.device = "BlackHole 34ch";
  s.options.numOutputBusChannels = 34;
  s.options.numInputBusChannels = 34;
  s.options.memSize = 8192 * 2 * 2 * 2;
  s.options.blockSize = 8;

  s.waitForBoot({
    s.meter();
    s.plotTree();
    SoundDesignEnvironment.start(());
  });

  s.boot();


}).value();
