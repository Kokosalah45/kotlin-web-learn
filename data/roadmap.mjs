// Structure derived from roadmap.sh/kotlin (https://roadmap.sh/kotlin.json) by
// resolving graph edges, section-box containment, and column/y clustering.
// 52 topics + 101 subtopics = 153 roadmap nodes, plus a few marked `extra: true`
// that the roadmap implies but does not draw as its own node.
//
// `docs` are slugs verified to exist on kotlinlang.org/docs/<slug>.html
// (checked against https://kotlinlang.org/sitemap.xml).
// `api` are paths under https://kotlinlang.org/api/core/.

export const DOCS = 'https://kotlinlang.org/docs/'
export const API = 'https://kotlinlang.org/api/core/'

export const sections = [
  {
    slug: 'language-basics',
    title: 'Language Basics',
    topics: [
      {
        slug: 'introduction-to-kotlin',
        title: 'Introduction to Kotlin',
        docs: ['getting-started'],
        subs: [
          { slug: 'history-of-kotlin', title: 'History of Kotlin', docs: ['faq'] },
          { slug: 'why-use-kotlin', title: 'Why use Kotlin', docs: ['comparison-to-java', 'faq'] },
          {
            slug: 'java-interoperability',
            title: 'Java Interoperability',
            docs: ['java-interop', 'java-to-kotlin-interop'],
          },
          {
            slug: 'setting-up-the-environment',
            title: 'Setting up the Environment',
            docs: ['getting-started', 'jvm-get-started'],
          },
        ],
      },
      { slug: 'code-organization', title: 'Code Organization', docs: ['coding-conventions', 'packages'] },
      { slug: 'main-function', title: 'main Function', docs: ['basic-syntax'] },
      {
        slug: 'variables',
        title: 'Variables',
        docs: ['basic-syntax'],
        subs: [{ slug: 'val-vs-var', title: 'val vs var', docs: ['basic-syntax', 'properties'] }],
      },
      {
        slug: 'data-types',
        title: 'Data types',
        docs: ['types-overview'],
        subs: [
          { slug: 'type-inference', title: 'Type Inference', docs: ['basic-syntax', 'types-overview'] },
          { slug: 'type-checks-and-casts', title: 'Type Checks & Casts', docs: ['typecasts'] },
        ],
      },
      {
        slug: 'basic-types',
        title: 'Basic Types',
        docs: ['types-overview'],
        isBox: true,
        subs: [
          { slug: 'integers', title: 'Integers', docs: ['numbers'], api: ['kotlin-stdlib/kotlin/-int/'] },
          { slug: 'floats', title: 'Floats', docs: ['numbers'], api: ['kotlin-stdlib/kotlin/-double/'] },
          {
            slug: 'unsigned-integers',
            title: 'Unsigned Integers',
            docs: ['unsigned-integer-types'],
            api: ['kotlin-stdlib/kotlin/-u-int/'],
          },
          { slug: 'characters', title: 'Characters', docs: ['characters'], api: ['kotlin-stdlib/kotlin/-char/'] },
          { slug: 'strings', title: 'Strings', docs: ['strings'], api: ['kotlin-stdlib/kotlin/-string/'] },
          { slug: 'booleans', title: 'Booleans', docs: ['booleans'], api: ['kotlin-stdlib/kotlin/-boolean/'] },
          { slug: 'arrays', title: 'Arrays', docs: ['arrays'], api: ['kotlin-stdlib/kotlin/-array/'] },
        ],
      },
      { slug: 'comments', title: 'Comments', docs: ['basic-syntax', 'kotlin-doc'] },
      {
        slug: 'collections',
        title: 'Collections',
        docs: ['collections-overview', 'constructing-collections'],
        subs: [
          {
            slug: 'lists',
            title: 'Lists',
            docs: ['list-operations'],
            api: ['kotlin-stdlib/kotlin.collections/-list/'],
          },
          { slug: 'sets', title: 'Sets', docs: ['set-operations'], api: ['kotlin-stdlib/kotlin.collections/-set/'] },
          { slug: 'maps', title: 'Maps', docs: ['map-operations'], api: ['kotlin-stdlib/kotlin.collections/-map/'] },
          { slug: 'read-only-vs-mutable', title: 'read-only vs mutable', docs: ['collections-overview'] },
          { slug: 'iterators', title: 'Iterators', docs: ['iterators'], api: ['kotlin-stdlib/kotlin.collections/-iterator/'] },
        ],
      },
      { slug: 'ranges', title: 'Ranges', docs: ['ranges'], api: ['kotlin-stdlib/kotlin.ranges/'] },
      {
        slug: 'progressions',
        title: 'Progressions',
        docs: ['ranges'],
        api: ['kotlin-stdlib/kotlin.ranges/-int-progression/'],
      },
      {
        slug: 'printing-data',
        title: 'Printing data',
        docs: ['basic-syntax'],
        subs: [
          {
            slug: 'print-and-println',
            title: 'print & println',
            docs: ['basic-syntax', 'read-standard-input'],
            api: ['kotlin-stdlib/kotlin.io/println.html'],
          },
          { slug: 'string-templates', title: 'String Templates', docs: ['strings'] },
        ],
      },
      { slug: 'sequences', title: 'Sequences', docs: ['sequences'], api: ['kotlin-stdlib/kotlin.sequences/-sequence/'] },
    ],
  },
  {
    slug: 'control-flow',
    title: 'Control Flow',
    topics: [
      {
        slug: 'conditional-expressions',
        title: 'Conditional Expressions',
        docs: ['control-flow'],
        subs: [
          { slug: 'if', title: 'if', docs: ['control-flow'] },
          { slug: 'when', title: 'when', docs: ['control-flow'] },
        ],
      },
      {
        slug: 'loops',
        title: 'Loops',
        docs: ['control-flow'],
        subs: [
          { slug: 'for', title: 'for', docs: ['control-flow'] },
          { slug: 'while', title: 'while', docs: ['control-flow'] },
          { slug: 'break-and-continue', title: 'break & continue', docs: ['returns'] },
          { slug: 'label-loops', title: 'Label Loops', docs: ['returns'] },
        ],
      },
      {
        slug: 'exceptions',
        title: 'Exceptions',
        docs: ['exceptions'],
        subs: [
          { slug: 'throwing-exceptions', title: 'Throwing Exceptions', docs: ['exceptions'] },
          { slug: 'catching-exceptions', title: 'Catching Exceptions', docs: ['exceptions'] },
        ],
      },
    ],
  },
  {
    slug: 'functions',
    title: 'Functions',
    topics: [
      {
        slug: 'standard-functions',
        title: 'Standard functions',
        docs: ['functions'],
        subs: [
          { slug: 'parameters', title: 'parameters', docs: ['functions'] },
          { slug: 'return', title: 'return', docs: ['functions', 'returns'] },
          { slug: 'varargs', title: 'varargs', docs: ['functions'] },
        ],
      },
      { slug: 'local-functions', title: 'Local Functions', docs: ['functions'] },
      { slug: 'member-functions', title: 'Member Functions', docs: ['functions', 'classes'] },
      { slug: 'tail-recursive-functions', title: 'Tail-recursive Functions', docs: ['functions'] },
      {
        slug: 'function-types',
        title: 'Function Types',
        docs: ['lambdas'],
        subs: [
          { slug: 'higher-order-functions', title: 'Higher-order Functions', docs: ['lambdas', 'inline-functions'] },
          { slug: 'lambda-functions', title: 'Lambda Functions', docs: ['lambdas'] },
          { slug: 'anonymous-functions', title: 'Anonymous Functions', docs: ['lambdas'] },
        ],
      },
      { slug: 'extension-functions', title: 'Extension Functions', docs: ['extensions'] },
      { slug: 'scope-functions', title: 'Scope Functions', docs: ['scope-functions'], extra: true },
      {
        slug: 'study-guide',
        title: 'Study Guide',
        docs: ['functions', 'lambdas', 'extensions', 'scope-functions'],
        extra: true,
      },
    ],
  },
  {
    slug: 'collection-operations',
    title: 'Collection Operations',
    topics: [
      { slug: 'overview', title: 'Operations Overview', docs: ['collection-operations'] },
      { slug: 'constructing-collections', title: 'Constructing Collections', docs: ['constructing-collections'] },
      { slug: 'transformations', title: 'Transformations', docs: ['collection-transformations'] },
      { slug: 'filtering', title: 'Filtering', docs: ['collection-filtering'] },
      { slug: 'plus-and-minus-operators', title: 'plus & minus Operators', docs: ['collection-plus-minus'] },
      { slug: 'grouping', title: 'Grouping', docs: ['collection-grouping'] },
      { slug: 'retrieving-collection-parts', title: 'Retrieving Collection Parts', docs: ['collection-parts'] },
      { slug: 'retrieving-single-elements', title: 'Retrieving single elements', docs: ['collection-elements'] },
      { slug: 'ordering', title: 'Ordering', docs: ['collection-ordering'] },
      { slug: 'aggregate-operations', title: 'Aggregate Operations', docs: ['collection-aggregate'] },
      { slug: 'write-operations', title: 'Write Operations', docs: ['collection-write'] },
      {
        slug: 'collection-specific-operations',
        title: 'Collection-specific Operations',
        isBox: true,
        docs: ['list-operations', 'set-operations', 'map-operations'],
        subs: [
          { slug: 'list-operations', title: 'Lists', docs: ['list-operations'] },
          { slug: 'set-operations', title: 'Sets', docs: ['set-operations'] },
          { slug: 'map-operations', title: 'Maps', docs: ['map-operations'] },
        ],
      },
      { slug: 'equality-and-hashing', title: 'Equality & Hashing', docs: ['equality'], extra: true },
      {
        slug: 'comparisons-and-comparators',
        title: 'Comparisons & Comparators',
        docs: ['collection-ordering'],
        api: ['kotlin-stdlib/kotlin.comparisons/'],
        extra: true,
      },
    ],
  },
  {
    slug: 'classes-and-objects',
    title: 'Classes & Objects',
    topics: [
      { slug: 'defining-classes', title: 'Defining Classes', docs: ['classes'] },
      { slug: 'creating-instances', title: 'Creating Instances', docs: ['classes'] },
      {
        slug: 'class-members',
        title: 'Class Members',
        docs: ['classes'],
        subs: [
          { slug: 'properties', title: 'Properties', docs: ['properties'] },
          { slug: 'methods', title: 'Methods', docs: ['functions', 'classes'] },
          { slug: 'constructors', title: 'Constructors', docs: ['classes'] },
          { slug: 'nested-and-inner-classes', title: 'Nested & Inner Classes', docs: ['nested-classes'] },
          { slug: 'object-declarations', title: 'Object Declarations', docs: ['object-declarations'] },
        ],
      },
      { slug: 'inheritance', title: 'Inheritance', docs: ['inheritance'] },
      { slug: 'extension-functions', title: 'Extension Functions', docs: ['extensions'] },
      { slug: 'visibility-modifiers', title: 'Visibility Modifiers', docs: ['visibility-modifiers'] },
      { slug: 'property-delegates', title: 'Property Delegates', docs: ['delegated-properties', 'delegation'] },
      {
        slug: 'more-on-classes-and-objects',
        title: 'More on Classes & Objects',
        isBox: true,
        docs: ['classes'],
        subs: [
          { slug: 'abstract-class', title: 'Abstract Class', docs: ['classes'] },
          { slug: 'interfaces', title: 'Interfaces', docs: ['interfaces', 'fun-interfaces'] },
          { slug: 'data-classes', title: 'Data Classes', docs: ['data-classes'] },
          { slug: 'sealed-class', title: 'Sealed Class', docs: ['sealed-classes'] },
          { slug: 'enum-class', title: 'Enum Class', docs: ['enum-classes'] },
          { slug: 'inline-class', title: 'Inline Class', docs: ['inline-classes'] },
          { slug: 'class-generics', title: 'Class Generics', docs: ['generics'] },
          { slug: 'type-aliases', title: 'Type Aliases', docs: ['type-aliases'] },
        ],
      },
    ],
  },
  {
    slug: 'null-safety',
    title: 'Null Safety',
    topics: [
      { slug: 'what-is-null-safety', title: 'What is Null Safety?', docs: ['null-safety'] },
      { slug: 'nullable-vs-non-nullable', title: 'Nullable vs Non-nullable', docs: ['null-safety'] },
      { slug: 'nullability-check-operators', title: 'Nullability Check Operators', docs: ['null-safety'] },
      { slug: 'safe-casts', title: 'Safe Casts', docs: ['typecasts', 'null-safety'] },
      {
        slug: 'java-nullability',
        title: 'Java Nullability Interop',
        docs: ['java-to-kotlin-nullability-guide', 'java-interop'],
        extra: true,
      },
    ],
  },
  {
    slug: 'async-programming',
    title: 'Async Programming',
    topics: [
      { slug: 'coroutines', title: 'Coroutines', docs: ['coroutines-overview', 'async-programming'] },
      { slug: 'suspending-functions', title: 'Suspending Functions', docs: ['coroutines-basics'] },
      {
        slug: 'coroutines-builders',
        title: 'Coroutines Builders',
        docs: ['coroutines-basics', 'composing-suspending-functions'],
      },
      {
        slug: 'coroutines-behavior',
        title: 'Coroutines Behavior',
        docs: ['coroutine-context-and-dispatchers', 'coroutines-cancellation'],
      },
      { slug: 'asynchronous-flow', title: 'Asynchronous Flow', docs: ['coroutines-flow', 'coroutines-flow-operators'] },
      {
        slug: 'coroutines-best-practices',
        title: 'Coroutines Best practices',
        docs: ['coroutines-guide', 'exception-handling', 'shared-mutable-state-and-concurrency'],
      },
      { slug: 'channels', title: 'Channels', docs: ['channels'], extra: true },
    ],
  },
  {
    slug: 'packages-and-ecosystem',
    title: 'Packages & Ecosystem',
    topics: [
      { slug: 'understanding-packages', title: 'Understanding Packages', docs: ['packages'] },
      { slug: 'importing-packages', title: 'Importing Packages', docs: ['packages'] },
      {
        slug: 'kotlin-libraries',
        title: 'Kotlin Libraries',
        docs: ['api-references'],
        subs: [
          { slug: 'test-library', title: 'Test Library', docs: ['jvm-test-using-junit'], api: ['kotlin-test/'] },
          { slug: 'coroutines-library', title: 'Coroutines', docs: ['coroutines-overview'] },
          { slug: 'serialization', title: 'Serialization', docs: ['serialization', 'serialization-get-started'] },
          { slug: 'io-library', title: 'I/O Library', docs: ['api-references'], api: ['kotlin-stdlib/kotlin.io/'] },
          { slug: 'date-and-time', title: 'Date & Time', docs: ['time-measurement'], api: ['kotlin-stdlib/kotlin.time/'] },
          { slug: 'jvm-metadata', title: 'JVM Metadata', docs: ['metadata-jvm'] },
          { slug: 'gradle-plugins', title: 'Gradle Plugins', docs: ['gradle', 'gradle-configure-project'] },
        ],
      },
      {
        slug: 'standard-library',
        title: 'Standard Library',
        docs: ['api-references'],
        api: ['kotlin-stdlib/'],
        subs: [
          { slug: 'default-imports', title: 'Default Imports', docs: ['packages'] },
          { slug: 'opt-in-requirements', title: 'Opt-in Requirements', docs: ['opt-in-requirements'] },
        ],
      },
      {
        slug: 'documentation',
        title: 'Documentation',
        docs: ['kotlin-doc'],
        subs: [
          { slug: 'kdoc', title: 'KDoc', docs: ['kotlin-doc'] },
          { slug: 'dokka', title: 'Dokka', docs: ['dokka-introduction', 'dokka-get-started'] },
        ],
      },
      {
        slug: 'build-tools',
        title: 'Build Tools',
        docs: ['gradle', 'maven'],
        subs: [
          { slug: 'gradle', title: 'Gradle', docs: ['gradle', 'gradle-configure-project', 'gradle-best-practices'] },
          { slug: 'maven', title: 'Maven', docs: ['maven', 'maven-configure-project'] },
          { slug: 'build-tool-api', title: 'Build Tool API', docs: ['build-tools-api'] },
        ],
      },
      {
        slug: 'ci-cd-tools',
        title: 'CI/CD Tools',
        docs: ['kotlin-and-ci'],
        subs: [{ slug: 'teamcity', title: 'TeamCity', docs: ['kotlin-and-ci'] }],
      },
      {
        slug: 'io',
        title: 'I/O',
        docs: ['read-standard-input'],
        api: ['kotlin-stdlib/kotlin.io/'],
        subs: [
          { slug: 'creating-files', title: 'Creating Files', docs: ['api-references'], api: ['kotlin-stdlib/kotlin.io/'] },
          {
            slug: 'writing-and-reading-files',
            title: 'Writing & Reading Files',
            docs: ['api-references'],
            api: ['kotlin-stdlib/kotlin.io/'],
          },
          { slug: 'buffered-streams', title: 'Buffered Streams', docs: ['api-references'], api: ['kotlin-stdlib/kotlin.io/'] },
        ],
      },
      {
        slug: 'ides',
        title: 'IDEs',
        docs: ['kotlin-ide'],
        subs: [
          { slug: 'intellij-idea', title: 'IntelliJ IDEA', docs: ['kotlin-ide'] },
          { slug: 'android-studio', title: 'Android Studio', docs: ['kotlin-ide', 'android-overview'] },
          { slug: 'kotlin-notebook', title: 'Kotlin Notebook', docs: ['kotlin-notebook-overview'] },
        ],
      },
    ],
  },
  {
    slug: 'kotlin-platforms',
    title: 'Kotlin Platforms',
    topics: [
      {
        slug: 'kotlin-multiplatform',
        title: 'Kotlin Multiplatform',
        docs: ['multiplatform/kmp-overview', 'multiplatform/quickstart'],
        subs: [
          { slug: 'compose-multiplatform', title: 'Compose Multiplatform', docs: ['multiplatform/compose-multiplatform'] },
        ],
      },
      {
        slug: 'kotlin-java',
        title: 'Kotlin/Java',
        docs: ['java-interop'],
        subs: [
          { slug: 'java-from-kotlin', title: 'Java from Kotlin', docs: ['java-interop'] },
          { slug: 'kotlin-from-java', title: 'Kotlin from Java', docs: ['java-to-kotlin-interop'] },
          {
            slug: 'spring-boot',
            title: 'Spring Boot',
            docs: ['jvm-get-started-spring-boot', 'jvm-create-project-with-spring-boot'],
          },
        ],
      },
      {
        slug: 'kotlin-native',
        title: 'Kotlin/Native',
        docs: ['native-overview', 'native-get-started'],
        subs: [
          { slug: 'c-interoperability', title: 'C Interoperability', docs: ['native-c-interop'] },
          { slug: 'swift-objc-interop', title: 'Swift/Objective-C Interop.', docs: ['native-objc-interop'] },
        ],
      },
      { slug: 'kotlin-wasm', title: 'Kotlin/WASM', docs: ['wasm-overview', 'wasm-get-started'] },
      { slug: 'kotlin-javascript', title: 'Kotlin/JavaScript', docs: ['js-overview', 'js-get-started'] },
      { slug: 'kotlin-scripting', title: 'Kotlin Scripting', docs: ['custom-script-deps-tutorial'] },
    ],
  },
  {
    slug: 'kotlin-applications',
    title: 'Kotlin Applications',
    topics: [
      {
        slug: 'android-development',
        title: 'Android Development',
        docs: ['android-overview'],
        external: 'https://roadmap.sh/android',
        subs: [
          { slug: 'android-jetpack', title: 'Android Jetpack', docs: ['android-overview'] },
          { slug: 'android-studio', title: 'Android Studio', docs: ['android-overview'] },
          { slug: 'android-sdk', title: 'Android SDK', docs: ['android-overview'] },
        ],
      },
      {
        slug: 'server-side-apps',
        title: 'Server-side Apps',
        docs: ['server-overview'],
        subs: [
          { slug: 'ktor', title: 'Ktor', docs: ['server-overview'], external: 'https://ktor.io/docs/' },
          { slug: 'spring', title: 'Spring', docs: ['jvm-get-started-spring-boot'] },
          { slug: 'quarkus', title: 'Quarkus', docs: ['server-overview'], external: 'https://quarkus.io/guides/kotlin' },
          { slug: 'vertx', title: 'Vert.x', docs: ['server-overview'], external: 'https://vertx.io/docs/vertx-core/kotlin/' },
        ],
      },
      {
        slug: 'data-analysis',
        title: 'Data Analysis',
        docs: ['data-analysis-overview'],
        subs: [
          {
            slug: 'kotlin-notebooks',
            title: 'Kotlin Notebooks',
            docs: ['kotlin-notebook-overview', 'get-started-with-kotlin-notebooks'],
          },
          { slug: 'kotlin-dataframe', title: 'Kotlin DataFrame', docs: ['data-analysis-libraries'] },
          { slug: 'kandy', title: 'Kandy', docs: ['data-analysis-visualization'] },
        ],
      },
      { slug: 'competitive-programming', title: 'Competitive Programming', docs: ['competitive-programming'] },
      {
        slug: 'ai-development',
        title: 'AI Development',
        docs: ['kotlin-ai-apps-development-overview'],
        subs: [
          { slug: 'koog', title: 'Koog', docs: ['kotlin-ai-apps-development-overview', 'kotlin-ai-skills'] },
        ],
      },
    ],
  },
]

export function allNodes() {
  const out = []
  for (const s of sections) {
    for (const t of s.topics) {
      out.push({
        ...t,
        kind: 'topic',
        section: s.slug,
        sectionTitle: s.title,
        path: `${s.slug}/${t.slug}`,
      })
      for (const sub of t.subs ?? []) {
        out.push({
          ...sub,
          kind: 'sub',
          section: s.slug,
          sectionTitle: s.title,
          parent: t.slug,
          parentTitle: t.title,
          path: `${s.slug}/${t.slug}/${sub.slug}`,
        })
      }
    }
  }
  return out
}

export function docUrl(slug) {
  return `${DOCS}${slug}.html`
}

export function apiUrl(path) {
  return `${API}${path}`
}
