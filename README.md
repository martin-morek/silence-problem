
## Assignment

Please write a command-line program which generates the segment descriptor we
need. The program should accept these command-line parameters:

* The path to an XML file with silence intervals
* The silence duration which reliably indicates a chapter transition
* The maximum duration of a segment, after which the chapter will be broken up into multiple segments
* A silence duration which can be used to split a long chapter (always shorter than the silence duration used to split chapters)

The program should output a list of segments in chronological order, following this JSON
structure:

```json
{
  "segments": [
    {
       "title": "Chapter 1, part 1",
       "offset": "PT0S"
    },
    {
       "title": "Chapter 1, part 2",
       "offset": "PT31M12S"
    },
    {
       "title": "Chapter 2",
       "offset": "PT47M20.5S"
    },
    {
       "title": "Chapter 3, part 1",
       "offset": "PT1H7M5S"
    },
    {
       "title": "Chapter 3, part 2",
       "offset": "PT1H30M12S"
    },
    {
       "title": "Chapter 3, part 3",
       "offset": "PT2H1M10S"
    }
  ]
}
```

The `title` field is optional, so only include this feature if time permits. We don't know
the actual chapter names, so we suggest naming them "Chapter 1", "Chapter 2" and so on.
If a chapter has been split up, we may title the segments like "Chapter 1, part 1".

Note that the initial segment starting at zero seconds must always be included.

You can choose to print the result to standard output or write it to a file.

You can assume the input XML is always valid and that its values make sense (no overlaps in
periods, no cases where "from" is higher than "until" etc), so you don't have to do a lot
of error handling. You can choose which programming language to use, and you should also
feel free to make any improvements or adjustments to the approach we have described, to
better support the end goal.

## How to build
To build executable package run in terminal:

```
sbt assembly
```

## How to run
To execute project (project has to be built before, step above) run in terminal:

```
scala <pathToExecutableJar> <pathToXmlInputFile> <chapterSilenceDuration> <maximumChapterDuration> <segmentPartDuration>
```

For example: 
```
scala target/scala-2.13/silence-problem-assembly-0.1.jar input-1.xml 5 30 3
```

### Program arguments
 1. pathToXmlInputFile - path to an XML file with silence intervals
 2. chapterSilenceDuration - silence duration which reliably indicates a chapter transition
 3. maximumChapterDuration - maximum duration of a segment, after which the chapter will be broken up into multiple segments
 4. segmentPartDuration - silence duration which can be used to split a long chapter

- All program arguments have to be provided
- All durations are in seconds
