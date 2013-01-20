Instr("cs.fx.Reverb", {
  arg bus, numChan, mix = 1.0;

  var in, out;

  in = In.ar(bus, numChan);

  out = LPF.ar(in, 360);

  out = Instr.ar("reverbs.reverberator3", (
    audio: in,
    revBalance: 1.0,
	 	revTime: 9.0,
	 	taps: 10,
	 	combs: 10,
	 	allpasses: 0,
	 	tapsMin: 0.01,
	 	tapsMax: 0.07,
	 	combsMin: 0.1,
	 	combsMax: 0.7,
	 	allpassMin: 0.01,
	 	allpassMax: 0.05,
	 	allpassDecay: 1.0
  ));

  out;
}, [
  \audiobus,
  ObjectSpec(1),
  \unipolar
]);
