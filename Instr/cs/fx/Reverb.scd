Instr("cs.fx.Reverb.reverberator", {
  arg bus, numChan, mix = 1.0, revTime = 9.0;

  var in, out;

  in = In.ar(bus, numChan);

  //out = LPF.ar(in, 360);

  out = Instr.ar("cs.fx.Reverb.reverberator3", (
    audio: in,
    revBalance: mix,
	 	revTime: revTime,
	 	taps: 10,
	 	combs: 10,
	 	allpasses: 0,
	 	tapsMin: 0.01,
	 	tapsMax: 0.27,
	 	combsMin: 0.1,
	 	combsMax: 0.27,
	 	allpassMin: 0.01,
	 	allpassMax: 0.15,
	 	allpassDecay: 1.0
  ));

  out;
}, [
  \audiobus,
  ObjectSpec(1),
  \unipolar
]);

/**
 *  Adapted from cxaudio.reverbs.reverberator3.  Basically does the same thing
 *  just ensures delay lengths are primes.
 **/
Instr("cs.fx.Reverb.reverberator3",{
	 arg audio, //stereo or mono
	 	revBalance=0.0,// 0..1
	 	
	 	revTime=4,
	 	taps=10,
	 	combs=6,
	 	allpasses=4,
	 	tapsMin=0.01,
	 	tapsMax=0.07,
	 	combsMin=0.3,
	 	combsMax=0.5,
	 	allpassMin=0.01,
	 	allpassMax=0.05,
	 	allpassDecay=1.0;
	
	var 	tapsOut,out,tapsLevelMax,combinput,combLevelMax,make_prime;

  /**
   *  Given an amount of seconds, return an amount of seconds where the
   *  amount of corresponding samples is a prime number.
   **/
  make_prime = {
    arg seconds;

    var samples, sampleRate = Server.default.sampleRate;

    // amount of samples corresponding to this number of seconds
    samples = (sampleRate * seconds).asInteger();

    // if even, make odd
    if (samples % 2 == 0, {
      samples = samples + 1;
    });

    // find next prime
    while ({ samples.isPrime() }, { samples = samples + 2; });

    // convert back to seconds
    (samples / sampleRate);
  };
	
	if(taps > 0,{
		tapsLevelMax = taps.reciprocal;

		tapsOut = 
			Mix.fill(taps,{
				var delays,samples;
				delays = make_prime.value(rrand(tapsMin,tapsMax));
				
        DelayN.ar(audio,delays,delays,tapsLevelMax.rand)
			}) + audio
	},{
		tapsOut = audio;
	});
	
	// 0 is permissable
	if(combs == 0,{
		out = tapsOut;
	},{
		combs.do({ arg ci;
			var times;
			times = make_prime.value(rrand(combsMin,combsMax));
			if(combinput.isNil, { //first time thru
				combLevelMax = combs.reciprocal; // first time, else possible div by 0
				combinput = tapsOut;
				out = CombC.ar(  
							combinput,times,times,
							revTime,combLevelMax.rand)
			},{
				out = out + CombC.ar( 
							combinput,times,times,
							revTime,combLevelMax.rand)
			})
		});
	});
	
	out = out * (Amplitude.kr(Mono(out)) > -160.dbamp);
	
	// 0 is permissable
	allpasses.do({ arg i;
		var times;

		times = [
      make_prime.value(rrand(allpassMin,allpassMax)),
      make_prime.value(rrand(allpassMin,allpassMax))
    ];

 		out = AllpassC.ar(
	 			out,
			 	// first time thru its possibly still mono, 
 				// then it expands to stereo and each subsequent is paired up to that expansion

	 			times,
	 			times,
	 			allpassDecay)
	 });
	 
	out = out * (Amplitude.kr(Mono(out)) > -160.dbamp);
	 
	XFader.ar(audio,out,revBalance);
	
},[
	\audio,
	\unipolar,
	[0,20],
	StaticIntegerSpec(0,20),
	StaticIntegerSpec(0,20),
	StaticIntegerSpec(0,20),
	StaticSpec(0.0001,0.5),
	StaticSpec(0.0001,1.0),
	StaticSpec(0.0001,0.5),
	StaticSpec(0.0001,1.0),
	StaticSpec(0.0001,0.5),
	StaticSpec(0.0001,1.0),
	ControlSpec(0,10.0)
],
	\stereo 
);

	
