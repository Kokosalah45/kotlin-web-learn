export function Logo() {
  return (
    <span className="x:flex x:items-center x:gap-2 x:font-semibold">
      <svg width="22" height="22" viewBox="0 0 60 60" aria-hidden="true">
        <defs>
          <linearGradient id="kt" x1="0" y1="60" x2="60" y2="0" gradientUnits="userSpaceOnUse">
            <stop offset="0" stopColor="#0095D5" />
            <stop offset=".3" stopColor="#238AD9" />
            <stop offset=".62" stopColor="#557BDE" />
            <stop offset=".86" stopColor="#7472E2" />
            <stop offset="1" stopColor="#806EE3" />
          </linearGradient>
        </defs>
        <path fill="url(#kt)" d="M60 60H0V0h60L30 30z" />
      </svg>
      <span>Kotlin Learn</span>
    </span>
  )
}
