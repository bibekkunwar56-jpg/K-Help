-- V4: Korea Information — curated guides for foreign residents.
-- Guides are read by everyone; only ADMIN accounts may create/edit/delete them.

CREATE TABLE guides (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slug VARCHAR(160) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    summary TEXT NOT NULL,
    body TEXT NOT NULL,
    topic VARCHAR(60) NOT NULL,
    source_url VARCHAR(500),
    published BOOLEAN NOT NULL DEFAULT TRUE,
    author_id UUID REFERENCES users(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_guides_topic ON guides(topic);
CREATE INDEX idx_guides_published ON guides(published);

INSERT INTO guides (slug, title, summary, body, topic, source_url) VALUES
(
    'alien-registration-card-arc',
    'Alien Registration Card (ARC): your first 90 days',
    'Most foreigners staying longer than 90 days must register at a local immigration office. This is the step-by-step.',
    E'1. Book an appointment\n'
    'Go to HiKorea (hikorea.go.kr) and book an "Alien Registration" visit at the immigration office for the district you live in. Walk-ins are slow; the booking slot is what you actually wait for.\n\n'
    '2. Bring the paperwork\n'
    '- Passport and visa (or entry confirmation)\n'
    '- Completed application form (download it from HiKorea, do not sign until asked)\n'
    '- One passport-style photo (3.5 x 4.5 cm)\n'
    '- Proof of where you live: lease contract, dormitory certificate, or a "confirmation of residence" from your landlord\n'
    '- Application fee (cash or card; the amount changes, check HiKorea before you go)\n\n'
    '3. After the visit\n'
    'You get a receipt and the card arrives by post in roughly 2 to 3 weeks. Collect it, then keep it with you at all times — it is your ID in Korea.\n\n'
    '4. Keep it up to date\n'
    'If you move, report the new address within 14 days. Address, employer, and school changes all need to be reported, either on HiKorea or in person.\n\n'
    'Common mistake: waiting until the 90 days are nearly over. Book early — appointment slots near the deadline are the ones that fill up first.',
    'visa-immigration',
    'https://www.hikorea.go.kr'
),
(
    'national-health-insurance-nhis',
    'National Health Insurance (NHIS) for foreign residents',
    'Long-stay visa holders are usually enrolled in the National Health Insurance Service. Here is how it works and what to do.',
    E'Who is enrolled\n'
    'Most people on long-stay visas are enrolled automatically once their alien registration is processed. Some visa types are covered as a dependent of a family member instead of paying separately. Students and workers may be covered through their school or employer.\n\n'
    'What you get\n'
    'Covered treatment at clinics and hospitals at the insured rate, which is far below the uninsured price. Dental, some check-ups, and cosmetic procedures are usually not covered.\n\n'
    'What to bring to an appointment\n'
    'Your ARC. The clinic looks your coverage up by the number on the card. If coverage has not started yet, you pay the full amount and can claim some of it back later.\n\n'
    'Paying\n'
    'Contributions are billed monthly. If you have no income, the amount is calculated from your visa status and estimated income. Missing payments can affect your visa extension, so keep the direct debit or payment app active.\n\n'
    'Not sure of your status?\n'
    'Call NHIS on 1577-1000 (foreign-language support is available) or check the English site. The immigration contact centre at 1345 can also explain which category you fall into.',
    'healthcare',
    'https://www.nhis.or.kr'
),
(
    'korean-bank-account-basics',
    'Opening a Korean bank account',
    'You can open an account at most banks once you have your ARC. Here is what to expect.',
    E'What you need\n'
    '- Your ARC (some banks accept a passport plus an alien registration receipt while you wait for the card)\n'
    '- A Korean phone number — this is required for the banking app and for one-time passwords\n'
    '- Proof of address, if the address on your ARC is not current\n\n'
    'Choosing a bank\n'
    'The large retail banks all offer similar accounts. For a first account, pick a branch near where you live or work and ask about the English-language service — larger branches near universities and business districts usually have staff who can help in English.\n\n'
    'What you get\n'
    'A debit card, online banking, and a banking app. Transfers between Korean accounts are usually instant and free or very cheap.\n\n'
    'Limits to expect\n'
    'Banks set daily transfer and ATM withdrawal limits, and they may start you on lower limits if your visa is short-term or you just arrived. Ask for the limit to be raised once you have salary deposits or a longer visa.\n\n'
    'Practical tips\n'
    '- Report your new account to your employer for salary deposits.\n'
    '- Keep your ARC details updated at the bank if you change address, visa, or phone number.\n'
    '- Never lend your account or card to anyone. Accounts used by a third party get frozen, and that can damage your visa record.',
    'money-banking',
    NULL
),
(
    'mobile-phone-plan-without-arc',
    'Getting a phone number before your ARC arrives',
    'You can get a working Korean number without an ARC — but the options are different from a normal plan.',
    E'Option 1: Prepaid SIM or eSIM at the airport\n'
    'Show your passport, pick a short-term plan (commonly 30 to 90 days), and you walk out with a working number. This is the fastest route and needs no ARC. Data allowances vary; check whether incoming calls are free, as some plans charge for them.\n\n'
    'Option 2: Prepaid or "foreigner" plans from MVNO carriers\n'
    'Smaller carriers resell network capacity and are noticeably cheaper than the big three. Many branches and airport counters will set up a prepaid plan for you with just a passport.\n\n'
    'Option 3: A normal postpaid contract\n'
    'This needs an ARC and usually a Korean bank account for automatic payment. It is the cheapest per gigabyte and the only option for family plans and phone instalments. Do this after your ARC arrives.\n\n'
    'Why it matters\n'
    'Almost every Korean online service — banking, delivery apps, online shopping, government portals — verifies you through a Korean phone number. Get one early even if you keep using your home SIM for calls.\n\n'
    'Watch out for\n'
    'Identity verification services often require a number registered in your own name. A number borrowed from a friend will block you at the next step.',
    'phone-internet',
    NULL
),
(
    'jeonse-wolse-deposit-guide',
    'Wolse and jeonse: how Korean rentals really work',
    'Korean housing does not work like most countries. Understand the deposit system and the checks to run before you pay anything.',
    E'The two main systems\n'
    '- Wolse (월세): a deposit plus monthly rent. The larger the deposit, the lower the monthly rent.\n'
    '- Jeonse (전세): a large lump-sum deposit instead of monthly rent, returned when you move out. Common for 2-year contracts, and much larger than a wolse deposit.\n\n'
    'Before you sign anything\n'
    '- Work with a licensed realtor (공인중개사 office). Their fee is regulated and they verify the property for you.\n'
    '- Ask for the building register (등기부등본). It shows who really owns the property and whether it is already mortgaged. If the deposit plus existing debt is close to the property value, walk away.\n'
    '- Confirm the owner on the register matches the person you are paying.\n\n'
    'Careful with jeonse\n'
    'Your deposit has priority over the landlord''s other debts only if you register your move-in date (전입신고) plus a fixed date stamp (확정일자) at the district office. Do both immediately after moving in — this is the single most important step.\n\n'
    'Moving in and out\n'
    'You must report your address change (전입신고) within 14 days of moving — the same rule that applies to your ARC address.\n\n'
    'Deposit protection\n'
    'In some cases you can join a deposit guarantee scheme through the Korea Housing Finance Corporation. Ask your realtor whether the property qualifies.',
    'housing',
    'https://www.khug.or.kr'
),
(
    'work-permit-rules-by-visa',
    'Who is allowed to work on which visa',
    'Working without the right permission can end your stay in Korea. Check your visa category before you accept any job.',
    E'Usually free to work\n'
    'F-series visas (F-2, F-4, F-5, F-6 and similar) generally allow employment without a separate work permit.\n\n'
    'Usually restricted\n'
    '- D-2 (student): part-time work is possible, but you need permission from the immigration office first, and there are limits on hours and on the type of workplace. Never start work before the permission is granted.\n'
    '- D-10 (job seeking): limited activities, check the conditions attached to your status.\n'
    '- E-9 (non-professional employment): you may only work for the employer named on your permit. Changing employers requires approval.\n'
    '- C-3 and other short-term visas: no work allowed.\n\n'
    'E-series professionals\n'
    'E-1 to E-7 are tied to a specific employer and role. If you change jobs, the new employer has to file for the change before you start.\n\n'
    'Why it matters\n'
    'Unauthorised work is recorded against you, and it can block future visa extensions, changes of status, and re-entry. If you are unsure, ask the immigration contact centre on 1345 before you sign anything — the call is free and the advice is official.',
    'jobs',
    'https://www.hikorea.go.kr'
),
(
    'immigration-contact-center-1345',
    'Calling 1345: the immigration hotline',
    '1345 answers immigration questions in many languages. It is the fastest official answer for most visa problems.',
    E'What it is\n'
    'The Foreigner Information Centre, run by the immigration service. Staff answer questions about visas, extensions, alien registration, and reporting changes.\n\n'
    'How to use it\n'
    'Dial 1345 from any Korean phone. Interpretation is offered in many languages including English, Chinese, Vietnamese, and others — say the language you need at the start of the call. Lines are busiest in the morning; early afternoon is usually quicker.\n\n'
    'Have ready\n'
    'Your ARC number (or passport number), your visa type, and your specific question. Write the question down before you call — it saves a lot of time.\n\n'
    'Good questions for 1345\n'
    '- When exactly do I need to extend my visa, and can I book online?\n'
    '- Am I allowed to take this part-time job on my visa?\n'
    '- I moved house — what do I need to report, and by when?\n'
    '- What documents do I need for a change of status?\n\n'
    'Both HiKorea (hikorea.go.kr) and 1345 are official and free. For day-to-day city questions — trash rules, buses, civil paperwork — 120 (Dasan Call Centre) is the other number worth saving.',
    'visa-immigration',
    'https://www.hikorea.go.kr/Main.pt'
),
(
    'free-korean-classes',
    'Learning Korean for free (and cheaply)',
    'Several government-run programmes teach Korean to foreign residents, and some of them count toward your visa points.',
    E'KIIP — Korea Immigration and Integration Programme\n'
    'The main government programme for foreign residents. It runs from beginner Korean through to an understanding-of-Korean-society course, and finishing stages gives you points toward certain visas and can replace part of the naturalisation test. Classes are free or almost free, but places are limited and you usually need to take a level test first.\n\n'
    'Multicultural Family Support Centres\n'
    'Located in most districts, they run Korean classes, often free, and usually with a mix of daytime and evening timetables. They also help with paperwork and counselling. Look up the centre for your district.\n\n'
    'Seoul Global Center and city programmes\n'
    'Metropolitan and district offices run free conversation classes and language exchange meetups. These are informal but good for speaking practice.\n\n'
    'Paid options that are still cheap\n'
    '- King Sejong Institute classes and their online courses\n'
    '- University language institutes (more expensive, faster pace, strong for academic or TOPIK preparation)\n\n'
    'Practical advice\n'
    'Book the level test early — KIIP intakes fill up. Combine a structured class with daily practice: reading signs and menus out loud on your commute does more for pronunciation than most textbooks.',
    'language',
    NULL
);
