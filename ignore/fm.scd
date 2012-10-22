
(
  Instr("fm", {
    arg freq = 440,
      amp = 0.9,
      gate = 0,
      carrierFreqRatio = 1.0,
      modulatorFreqRatio = 1.0,
      carrierADSR = [0.05, 0.1, 0.5 * amp, 0.1],
      modulatorADSR = [0.05, 0.1, 0.5 * amp, 0.1];

    var carrier,
      modulator,
      carrierEnvShape,
      carrierEnv,
      modEnvShape,
      modEnv,
      out;

    modEnvShape = Env.adsr(
      attackTime: modulatorADSR[0],
      decayTime: modulatorADSR[1],
      sustainLevel: modulatorADSR[2],
      releaseTime: modulatorADSR[3],
      peakLevel: amp,
      curve: [4, -4, -2]
    );
    
    modEnv = EnvGen.kr(
      envelope: modEnvShape,
      gate: gate
    );
    
    modulator = modEnv * SinOsc.ar(freq * modulatorFreqRatio);
    
    carrierEnvShape = Env.adsr(
      attackTime: carrierADSR[0],
      decayTime: carrierADSR[1],
      sustainLevel: carrierADSR[2],
      releaseTime: carrierADSR[3],
      peakLevel: amp,
      curve: [4, -4, -2]
    );
    // carrier sustains until noteoff
    carrierEnvShape.releaseNode = 2;

    carrierEnv = EnvGen.kr(
      envelope: carrierEnvShape,
      gate: gate
    );

    carrier = carrierEnv * SinOsc.ar((freq * carrierFreqRatio) + (modulator * freq));

    // free synth when both carrier and modulator envelopes are done
    FreeSelf.kr(Done.kr(carrierEnv) + Done.kr(modEnv) - 1);

    out = carrier;
  });

  
  Instr("fm.simplebass", {
    arg freq = 220,
      amp = 0.9,
      gate = 0;

    var outEnv,
      outEnvShape,
      core,
      mod,
      modFreq;

    outEnvShape = Env.adsr(
      attackTime: 0.5,
      decayTime: 0.5,
      sustainLevel: 0.75 * amp,
      releaseTime: 0.5,
      peakLevel: amp,
      curve: -4
    );
    outEnvShape.releaseNode = 1;

    outEnv = EnvGen.kr(
      envelope: outEnvShape, 
      gate: gate,
      doneAction: 2);
   
    /*modFreq = freq * 1.5;*/
    /*modFreq = MouseX.kr(0.0, 1.0).poll * (freq * 1.5);*/
    mod = SinOsc.ar(1.5 * freq, amp * 0.25);
    core = SinOsc.ar(freq, 0.5 * amp);
    core = SinOsc.ar(freq + (mod * freq), 0.5 * amp) + core;

    outEnv * core;
  });
  
  Instr("fm.bass", {
    arg freq = 220,
      amp = 0.9,
      gate = 0,
      // frequency of beating between tones (Hz)
      fBeats = 1.0;

    var l,
      r,
      rFreq,
      out;

    // rFreq is above left by a half of a semitone
    /*rFreq = freq * (2**(0.5/12));*/
    rFreq = freq + fBeats;
    /*rFreq = freq * 2;*/

    l = Instr.at("fm.simplebass").ar(freq, 0.5 * amp, gate);
    r = Instr.at("fm.simplebass").ar(rFreq, 0.5 * amp, gate);

    /*out = Pan2.ar(l, -0.8) + Pan2.ar(r, 0.8);*/
    out = l;

    /*out = CrossoverDistortion.ar(*/
      /*out,*/
      /*MouseX.kr(0, 1),*/
      /*MouseY.kr(0, 1)*/
    /*);*/

    Out.ar(0, out);
  });
)
