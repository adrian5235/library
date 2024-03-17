import { createApp } from 'vue';
import App from './App.vue';
import router from './routers'
import PrimeVue from 'primevue/config';
import DataTable from 'primevue/datatable';
import DataView from 'primevue/dataview';
import Column from 'primevue/column';
import Row from 'primevue/row';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Toolbar from 'primevue/toolbar';
import Dialog from 'primevue/dialog';
import Toast from 'primevue/toast';
import ToastService from 'primevue/toastservice';
import Textarea from 'primevue/textarea';
import MultiSelect from 'primevue/multiselect';
import Paginator from 'primevue/paginator';
import Dropdown from 'primevue/dropdown';
import Tag from 'primevue/tag';
import TreeTable from 'primevue/treetable';
import Calendar from 'primevue/calendar';
import InputSwitch from 'primevue/inputswitch';
import Image from 'primevue/image';
import FileUpload from 'primevue/fileupload';
import RadioButton from 'primevue/radiobutton';
import InputNumber from 'primevue/inputnumber';
import Accordion from 'primevue/accordion';
import AccordionTab from 'primevue/accordiontab';
import Menubar from 'primevue/menubar';
import Badge from 'primevue/badge';
import Ripple from 'primevue/ripple';
import Avatar from 'primevue/avatar';
import Menu from 'primevue/menu';
import Editor from 'primevue/editor';

import './assets/app.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import "primevue/resources/themes/lara-light-indigo/theme.css";
import "primevue/resources/primevue.min.css";
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';

const app = createApp(App);
app.use(router);
app.use(PrimeVue, {
  ripple: true,
  locale: {
    // filters
    startsWith: 'Starts with',
    contains: 'Contains',
    notContains: 'Not contains',
    endsWith: 'Ends with',
    equals: 'Equals',
    notEquals: 'Not equals',
    noFilter: 'No filter',

    // calendar
    dayNamesMin: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    dayNamesShort: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
    monthNamesShort: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
    dateFormat: 'dd/mm/yy',
    firstDayOfWeek: 0,
    today: 'Today',
    clear: 'Clear',

    // aria
    aria: ''
  },
});
app.use(ToastService);

app.directive('ripple', Ripple);

app.component("DataTable", DataTable);
app.component("DataView", DataView);
app.component("Column", Column);
app.component("Row", Row);
app.component("Button", Button);
app.component("InputText", InputText);
app.component("Toolbar", Toolbar);
app.component("Dialog", Dialog);
app.component("Toast", Toast);
app.component("Textarea", Textarea);
app.component("MultiSelect", MultiSelect);
app.component("Paginator", Paginator);
app.component("Dropdown", Dropdown);
app.component("Tag", Tag);
app.component("TreeTable", TreeTable);
app.component("Calendar", Calendar);
app.component("InputSwitch", InputSwitch);
app.component("Image", Image);
app.component("FileUpload", FileUpload);
app.component("RadioButton", RadioButton);
app.component("InputNumber", InputNumber);
app.component("Accordion", Accordion);
app.component("AccordionTab", AccordionTab);
app.component("Menubar", Menubar);
app.component("Badge", Badge);
app.component("Avatar", Avatar);
app.component("Menu", Menu);
app.component("Editor", Editor);

app.mount('#app');