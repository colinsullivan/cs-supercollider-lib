Instr("cs.synths.SuperSaw.Osc", {
  arg freq = 440, gate = 1, amp=0.5, mix=0.75, detune = 0.75;
  var detuneCurve, centerGain, sideGain, center, freqs, side, out, env, envShape;
	detuneCurve = { |x|
		(10028.7312891634*x.pow(11)) -
		(50818.8652045924*x.pow(10)) +
		(111363.4808729368*x.pow(9)) -
		(138150.6761080548*x.pow(8)) +
		(106649.6679158292*x.pow(7)) -
		(53046.9642751875*x.pow(6)) +
		(17019.9518580080*x.pow(5)) -
		(3425.0836591318*x.pow(4)) +
		(404.2703938388*x.pow(3)) -
		(24.1878824391*x.pow(2)) +
		(0.6717417634*x) +
		0.0030115596
	};
  centerGain = { |x| (-0.55366 * x) + 0.99785 };
  sideGain = { |x| (-0.73764 * x.pow(2)) + (1.2841 * x) + 0.044372 };

  center = LFSaw.ar(freq, Rand());
  freqs = [
    (freq - (freq*(detuneCurve.(detune))*0.11002313)),
    (freq - (freq*(detuneCurve.(detune))*0.06288439)),
    (freq - (freq*(detuneCurve.(detune))*0.01952356)),
    // (f + (f*(~detuneCurve.(detune))*0)),
    (freq + (freq*(detuneCurve.(detune))*0.01991221)),
    (freq + (freq*(detuneCurve.(detune))*0.06216538)),
    (freq + (freq*(detuneCurve.(detune))*0.10745242))
  ];
  side = Mix.fill(6, { |n|
    LFSaw.ar(freqs[n], Rand(0, 2))
  });

  out = (center * centerGain.(mix)) + (side * sideGain.(mix));

  envShape = Env.adsr(
    attackTime: 0.01,
    decayTime: 0.3,
    sustainLevel: 0.7,
    releaseTime: 1.0
  );

  env = EnvGen.kr(envShape, gate, levelScale: amp, doneAction: 2);

  out = env * out;
}, [
  \freq,
  \gate,
  \amp,
  \unipolar,
  \unipolar
]);

Instr("cs.synths.SuperSaw.Voice", {

  arg freq, gate, amp, mix, detune;
  var outL, outR;

  outL = Instr.ar("cs.synths.SuperSaw.Osc", (
    freq: freq,
    gate: gate,
    amp: amp,
    mix: mix,
    detune: detune,
  ));

  outR = Instr.ar("cs.synths.SuperSaw.Osc", (
    freq: freq,
    gate: gate,
    amp: amp,
    mix: mix,
    detune: detune,
  ));

  [outL, outR];

}, [
  \freq,
  \gate,
  \amp,
  \unipolar,
  \unipolar,
]);

Instr("cs.synths.SuperSaw.Filter", {

  arg bus, cutoffFreq;
  var out;

  out = In.ar(bus, 2);
  out = LPF.ar(out, cutoffFreq);

  out;

}, [
  \bus,
  \freq,
]);
