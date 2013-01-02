Instr("cs.sfx.Play", {

  arg buf;


  "hello".postln;
  buf.postln;
  buf.numChannels.postln;

  PlayBuf.ar(buf.numChannels, buf, doneAction: 2);

});
